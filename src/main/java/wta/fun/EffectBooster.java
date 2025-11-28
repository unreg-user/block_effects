package wta.fun;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import wta.fun.mathHelp.RandomH;
import wta.fun.serializHelp.SerializableO;

import java.util.*;

public record EffectBooster(StatusEffect effect, BoosterType bType, float value, BoosterFor bFor) implements SerializableO<NbtCompound, EffectBooster> {
	public enum BoosterType{
		MULTIPLY_DURATION("md", "duration"),
		MULTIPLY_AMPLIFIER("ma", "amplifier");

		public static final BoosterType[] all_typesBT=BoosterType.values();
		public static final HashMap<String, BoosterType> all_ser=new HashMap<>();

		private final String name;
		public final String chatName;

		BoosterType(String name, String chatName){
			this.name=name;
			this.chatName=chatName;
		}

		static {
			for (BoosterType type : all_typesBT){
				all_ser.put(type.name, type);
			}
		}
	}

	public enum BoosterFor{
		MONSTER(1, "m", "monsters"),
		NOT_MONSTER(1, "nm", "not monsters"),
		ALL(3, "a", "all");

		public static final BoosterFor[] all_typesBF=values();
		public static final HashMap<String, BoosterFor> all_ser=new HashMap<>();
		private static final int sumWeights;
		private static final int[] listWeights;

		private final int weight;
		private final String name;
		public final String chatName;

		BoosterFor(int weight, String name, String chatName){
			this.weight=weight;
			this.name=name;
			this.chatName=chatName;
		}

		public static BoosterFor getRandom(Random random){
			return RandomH.getRandom(random, sumWeights, listWeights, all_typesBF);
		}

		static {
			listWeights=Arrays.stream(all_typesBF).mapToInt(bf -> bf.weight).toArray();
			sumWeights=Arrays.stream(listWeights).sum();
			for (BoosterFor type : all_typesBF){
				all_ser.put(type.name, type);
			}
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

		public static EffectBoosters deserialize(NbtCompound element) {
			Identifier blockId=Identifier.tryParse(element.getString("bl"));
			if (blockId==null) return null;
			Block block=Registries.BLOCK.get(blockId);
			if (block==Blocks.AIR) return null;

			List<EffectBooster> boosters=new ArrayList<>();
			NbtList bList=element.getList("bs", NbtList.COMPOUND_TYPE);
			bList.stream().map(element1 -> {
				if (!(element1 instanceof NbtCompound compound)) return null;
				return EffectBooster.deserialize(compound);
			}).filter(Objects::nonNull).forEach(boosters::add);

			return new EffectBoosters(block, boosters);
		}
	}

	@Override
	public NbtCompound serialize() {
		NbtCompound element=new NbtCompound();
		Identifier effectId=Registries.STATUS_EFFECT.getId(effect);
		if (effectId==null) return null;
		element.putString("ef", effectId.toString());
		element.putString("bT", bType.name);
		element.putFloat("val", value);
		element.putString("bF", bFor.name);
		return element;
	}

	public static EffectBooster deserialize(NbtCompound element) {
		Identifier effectId=Identifier.tryParse(element.getString("ef"));
		if (effectId==null) return null;
		RegistryEntry<StatusEffect> effectEntry=Registries.STATUS_EFFECT.getEntry(effectId).orElse(null);
		if (effectEntry==null) return null;
		StatusEffect effect=effectEntry.value();

		BoosterType bType=BoosterType.all_ser.get(element.getString("bT"));
		float value=element.getFloat("val");
		BoosterFor bFor=BoosterFor.all_ser.get(element.getString("bF"));
		if (bType==null || bFor==null) return null;

		return new EffectBooster(effect, bType, value, bFor);
	}
}
