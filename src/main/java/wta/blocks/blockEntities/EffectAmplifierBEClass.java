package wta.blocks.blockEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import wta.blocks.BlocksInit;
import wta.fun.EffectBooster;
import wta.fun.mathHelp.RandomH;

import static wta.Block_effects.allEffectList;
import static wta.fun.EffectBooster.EffectBoosters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static wta.fun.ChooseEffectSystem2.radiusUE;

public class EffectAmplifierBEClass extends BlockEntity {
	private ArrayList<EffectBoosters> boosters=new ArrayList<>();

	public EffectAmplifierBEClass(BlockPos pos, BlockState state) {
		super(BlocksInit.effectAmplifierBE, pos, state);
	}

	@Override
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
	}

	@Override
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.put("boosters", boosters);
	}

	public void onPlaced(){
		if (world == null) return;

		HashSet<Block> blocks=new HashSet<>();
		HashMap<Block, EffectBoosters> boostersMap=new HashMap<>();
		boosters=new ArrayList<>();
		Random random=world.getRandom();

		for (int ix = -radiusUE; ix <= radiusUE; ix++) {
			for (int iy = -radiusUE; iy <= radiusUE; iy++) {
				for (int iz = -radiusUE; iz <= radiusUE; iz++) {
					BlockPos posI=pos.add(ix, iy, iz);
					Block block = world.getBlockState(posI).getBlock();
					blocks.add(block);
				}
			}
		}

		for (Block block : blocks){
			boostersMap.put(block, new EffectBoosters(block, new ArrayList<>()));
		}

		RandomH.forGroupRoundRandom(
			  random,
			  allEffectList.size(),
			  new ArrayList<>(blocks),
			  block -> boostersMap.get(block).boosters().add(generateBooster(random))
		);

		/*/for (Block block : blocks){
			boosters.add(new EffectBooster(
				  allEffectList.get(random.nextInt(allEffectList.size())),
				  block,
				  all_types.get(random.nextInt(all_types.size())),
				  (float) random.nextTriangular(0.5D, 0.5D)
			));
		}/*/
	}

	private static EffectBooster generateBooster(Random random){
		return new EffectBooster(
			  RandomH.getRandom(random, allEffectList),
			  RandomH.getRandom(random, EffectBooster.BoosterType.all_typesBT),
			  (float) random.nextTriangular(1, 1),
			  EffectBooster.BoosterFor.getRandom(random)
		);
	}
}
