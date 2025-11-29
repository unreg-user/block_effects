package wta.blocks.blockEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import wta.blocks.BlocksInit;
import static wta.fun.EffectBooster.BoosterFor;
import wta.fun.EffectBooster;
import wta.fun.FloatEffectInstance;
import wta.fun.mathHelp.RandomH;
import wta.fun.serializHelp.SerializeFun;

import java.util.*;

import static wta.Block_effects.allEffectList;
import static wta.fun.EffectBooster.EffectBoosters;
import static wta.fun.chooseSystem.ContextualChooseEffS.radiusAmp;

public class EffectAmplifierBEClass extends BlockEntity {
	private ArrayList<EffectBoosters> boosters=new ArrayList<>();
	private HashMap<StatusEffect, ArrayList<EffectBooster>> activeBoosters=new HashMap<>();
	public boolean isInitialized=false;

	public EffectAmplifierBEClass(BlockPos pos, BlockState state) {
		super(BlocksInit.effectAmplifierBE, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		NbtList boostersNbtList=nbt.getList("boosters", NbtList.COMPOUND_TYPE);
		boosters=new ArrayList<>(
			  boostersNbtList.stream()
				    .map(e -> EffectBoosters.deserialize((NbtCompound) e))
				    .filter(Objects::nonNull)
				    .toList()
		);
		isInitialized=nbt.getBoolean("isInit");
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.put("boosters", SerializeFun.serList(boosters));
		nbt.putBoolean("isInit", isInitialized);
	}

	public boolean onPlaced(PlayerEntity player){
		if (world == null || isInitialized){
			player.sendMessage(getBoostsText(), false);
			return false;
		}

		HashSet<Block> blocks=new HashSet<>();
		HashMap<Block, EffectBoosters> boostersMap=new HashMap<>();
		boosters=new ArrayList<>();
		Random random=world.getRandom();

		for (int ix = -radiusAmp; ix <= radiusAmp; ix++) {
			for (int iy = -radiusAmp; iy <= radiusAmp; iy++) {
				for (int iz = -radiusAmp; iz <= radiusAmp; iz++) {
					BlockPos posI=pos.add(ix, iy, iz);
					Block block = world.getBlockState(posI).getBlock();
					blocks.add(block);
				}
			}
		}

		blocks.remove(Blocks.AIR);
		blocks.remove(BlocksInit.diffuserB);
		blocks.remove(BlocksInit.effectAmplifierB);
		blocks.remove(BlocksInit.radiusEffectB);

		for (Block block : blocks){
			boostersMap.put(block, new EffectBoosters(block, new ArrayList<>()));
		}

		RandomH.forGroupRoundRandom(
			  random,
			  allEffectList.size(),
			  new ArrayList<>(blocks),
			  block -> boostersMap.get(block).boosters().add(generateBooster(random))
		);

		boosters=new ArrayList<>(boostersMap.values());
		updateActiveBoosters();

		isInitialized=true;
		player.sendMessage(Text.literal("effect amplifier block active"), false);
		return true;
	}

	public void updateActiveBoosters(){
		if (world == null) return;
		activeBoosters = new HashMap<>();
		HashSet<Block> neighbours=getNeighbour();
		for (EffectBoosters bsI : boosters){
			if (neighbours.contains(bsI.block())){
				for (EffectBooster bI : bsI.boosters()){
					activeBoosters.computeIfAbsent(bI.effect(), key -> new ArrayList<>()).add(bI);
				}
			}
		}
	}

	public void boost(FloatEffectInstance instanceF, boolean isMonster){
		ArrayList<EffectBooster> boostersForEffect=activeBoosters.getOrDefault(instanceF.effect, null);
		if (boostersForEffect == null) return;
		for (EffectBooster bI : boostersForEffect){
			BoosterFor bFor=bI.bFor();
			if ((bFor == BoosterFor.ALL) || (isMonster & bFor == BoosterFor.MONSTER) || (!isMonster & bFor == BoosterFor.NOT_MONSTER)){
				switch (bI.bType()){
					case MULTIPLY_DURATION -> instanceF.duration *= bI.value();
					case MULTIPLY_AMPLIFIER -> instanceF.amplifier *= bI.value();
				}
			}
		}
	}

	private HashSet<Block> getNeighbour(){
		if (world == null) return new HashSet<>();

		return new HashSet<>(List.of(
			  world.getBlockState(pos.up()).getBlock(),
			  world.getBlockState(pos.down()).getBlock(),
			  world.getBlockState(pos.south()).getBlock(),
			  world.getBlockState(pos.north()).getBlock(),
			  world.getBlockState(pos.east()).getBlock(),
			  world.getBlockState(pos.west()).getBlock()
		));
	}

	public Text getBoostsText(){
		HashSet<Block> neighbours=getNeighbour();
		MutableText text = Text.literal("effect amplifier block in ")
			  .append(getPosText())
			  .append("   has multipliers:");
		for (EffectBoosters bsI : boosters){
			text.append("\n  ")
				  .append(Text.translatable(bsI.block().getTranslationKey())
					    .styled(style -> neighbours.contains(bsI.block()) ?
						      style.withColor(Formatting.BLUE) :
						      style.withColor(Formatting.GRAY)
						      ))
				  .append(":");
			int lenBSI=bsI.boosters().size();
			for (int i=0; i<lenBSI; i++) {
				EffectBooster bI=bsI.boosters().get(i);
				text.append("\n        "+(i+1)+".")
					  .append(Text.translatable(bI.effect().getTranslationKey()).styled(style -> style.withColor(Formatting.AQUA))) //effect
					  .append(" ").append(Text.literal(bI.bType().chatName).styled(style -> style.withColor(Formatting.RED))) //type
					  .append(" ").append(Text.literal(String.valueOf( bI.value() )).styled(style -> style.withColor(Formatting.GREEN))) //value
					  .append(" times for ").append(Text.literal(bI.bFor().chatName).styled(style -> style.withColor(Formatting.RED))); //type for
			}
		}
		return text;
	}

	private Text getPosText(){
		BlockPos pos = this.getPos();
		return Text.literal(String.format(
					  "[%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())
			  )
			  .styled(
					style -> style.withColor(Formatting.BLUE)
			  );
	}

	private static EffectBooster generateBooster(Random random){
		return new EffectBooster(
			  RandomH.getRandom(random, allEffectList),
			  RandomH.getRandom(random, EffectBooster.BoosterType.all_typesBT),
			  random.nextFloat()*2.5F,
			  EffectBooster.BoosterFor.getRandom(random)
		);
	}
}
