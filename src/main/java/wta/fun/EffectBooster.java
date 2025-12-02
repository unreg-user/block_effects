package wta.fun;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import wta.fun.mathHelp.RandomH;
import wta.fun.serializHelp.SerializableO;

import java.util.*;

public record EffectBooster(StatusEffect effect, BoosterType bType, float value, BoosterFor bFor) implements SerializableO<NbtCompound, EffectBooster> {
	public enum BoosterType{
		MULTIPLY_DURATION("block_effects.boosters.type.to_duration"),
		MULTIPLY_AMPLIFIER("block_effects.boosters.type.to_amplifier");

		public static final BoosterType[] all_typesBT=BoosterType.values();
		public final String translationKey;

		BoosterType(String translationKey){
			this.translationKey=translationKey;
		}
	}

	public enum BoosterFor{
		MONSTER(1, "block_effects.boosters.for.to_monsters"),
		NOT_MONSTER(1, "block_effects.boosters.for.to_not_monsters"),
		ALL(3, "block_effects.boosters.for.to_all");

		public static final BoosterFor[] all_typesBF=values();
		private static final int sumWeights;
		private static final int[] listWeights;

		private final int weight;
		public final String translationKey;

		BoosterFor(int weight, String translationKey){
			this.weight=weight;
			this.translationKey=translationKey;
		}

		public static BoosterFor getRandom(Random random){
			return RandomH.getRandom(random, sumWeights, listWeights, all_typesBF);
		}

		static {
			listWeights=Arrays.stream(all_typesBF).mapToInt(bf -> bf.weight).toArray();
			sumWeights=Arrays.stream(listWeights).sum();
		}
	}

	public record EffectBoosters(Block block, List<EffectBooster> boosters) implements SerializableO<NbtCompound, EffectBoosters>{
		@Override
		public NbtCompound serialize() {
			NbtCompound element=new NbtCompound();
			Identifier blockId=Registries.BLOCK.getId(block);
			element.putString("bl", blockId.toString());

			List<NbtElement> boostersList=boosters.stream()
				  .map(EffectBooster::serialize)
				  .filter(Objects::nonNull)
				  .map(el -> (NbtElement) el)
				  .toList();
			NbtList elementBoosters=new NbtList(boostersList, NbtElement.COMPOUND_TYPE);
			element.put("bs", elementBoosters);

			return element;
		}

		public static EffectBoosters deserialize(NbtCompound element, SerMap map) {
			Identifier blockId=Identifier.tryParse(element.getString("bl"));
			if (blockId==null) return null;
			Block block=Registries.BLOCK.get(blockId);
			if (block==Blocks.AIR) return null;

			List<EffectBooster> boosters=new ArrayList<>();
			NbtList bList=element.getList("bs", NbtList.COMPOUND_TYPE);
			bList.stream().map(element1 -> {
				if (!(element1 instanceof NbtCompound compound)) return null;
				return EffectBooster.deserialize(compound, map);
			}).filter(Objects::nonNull).forEach(boosters::add);

			return new EffectBoosters(block, boosters);
		}
	}

	@Override
	public NbtCompound serialize() {
		NbtCompound element=new NbtCompound();
		Identifier effectId=Registries.STATUS_EFFECT.getId(effect);
		if (effectId==null) return null;
		element.putString("e", effectId.toString());
		element.putByte("t", (byte) bType.ordinal());
		element.putFloat("v", value);
		element.putByte("f", (byte) bFor.ordinal());
		return element;
	}

	public static EffectBooster deserialize(NbtCompound element, SerMap map) {
		Identifier effectId=Identifier.tryParse(element.getString("e"));
		if (effectId==null) return null;
		RegistryEntry<StatusEffect> effectEntry=Registries.STATUS_EFFECT.getEntry(effectId).orElse(null);
		if (effectEntry==null) return null;
		StatusEffect effect=effectEntry.value();

		BoosterType bType=map.types.get(element.getByte("t"));
		float value=element.getFloat("v");
		BoosterFor bFor=map.fors.get(element.getByte("f"));
		if (bType==null || bFor==null) return null;

		return new EffectBooster(effect, bType, value, bFor);
	}

	public static NbtCompound getSerMapNBT(){
		NbtCompound compound=new NbtCompound();

		NbtList types=new NbtList();
		for (BoosterType typeI : BoosterType.values()){
			types.add(NbtString.of(typeI.toString()));
		}
		compound.put("type", types);

		NbtList fors=new NbtList();
		for (BoosterFor typeI : BoosterFor.values()){
			fors.add(NbtString.of(typeI.toString()));
		}
		compound.put("for", fors);

		return compound;
	}

	public static SerMap getSerMap(NbtCompound compound){
		ArrayList<BoosterType> types = new ArrayList<>();
		NbtList typesList = compound.getList("type", NbtElement.STRING_TYPE);
		for (NbtElement element : typesList) {
			String name = element.asString();
			try {
				BoosterType type = BoosterType.valueOf(name);
				types.add(type);
			} catch (IllegalArgumentException e) {
				types.add(null);
			}
		}

		ArrayList<BoosterFor> fors = new ArrayList<>();
		NbtList forList = compound.getList("for", NbtElement.STRING_TYPE);
		for (NbtElement element : forList) {
			String name = element.asString();
			try {
				BoosterFor forType = BoosterFor.valueOf(name);
				fors.add(forType);
			} catch (IllegalArgumentException e) {
				fors.add(null);
			}
		}

		return new SerMap(types, fors);
	}

	public record SerMap(ArrayList<BoosterType> types, ArrayList<BoosterFor> fors){

	}
}
