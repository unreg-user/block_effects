package wta.fun;

import com.llamalad7.mixinextras.lib.semver.util.Stream;
import net.minecraft.block.Block;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record EffectBooster(StatusEffect effect, BoosterType bType, float value, BoosterFor bFor) implements SerializableO<NbtCompound, EffectBooster> {
	public enum BoosterType{
		MULTIPLY_DURATION,
		MULTIPLY_AMPLIFIER;
		public static BoosterType[] all_typesBT=BoosterType.values();
	}

	public enum BoosterFor{
		MONSTER(1),
		NOT_MONSTER(1),
		ALL(3);

		public static final BoosterFor[] all_typesBF=values();
		private static final int sumWeights;
		private static final int[] listWeights;

		private final int weight;

		BoosterFor(int weight){
			this.weight=weight;
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
			element.putString("block", blockId.toString());

			List<NbtCompound> boostersList=boosters.stream()
				  .map(EffectBooster::serialize)
				  .filter(Objects::nonNull)
				  .toList();
			NbtList elementBoosters=new NbtList(boostersList, NbtElement.COMPOUND_TYPE);
			element.put("boosters", elementBoosters);

			return element;
		}

		public static EffectBoosters deserialize(NbtCompound element) {
			Identifier blockId=Identifier.tryParse(element.getString("block"));
			if (blockId==null) return null;
			Block block=Registries.BLOCK.get(blockId);

			List<EffectBooster> boosters=new ArrayList<>();
			NbtList bList=element.getList("boosters", NbtList.COMPOUND_TYPE);
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
		element.putString("effect", effectId.toString());
		element.putString("bType", bType.toString());
		element.putFloat("value", value);
		element.putString("bFor", bFor.toString());
		return element;
	}

	public static EffectBooster deserialize(NbtCompound element) {
		Identifier effectId=Identifier.tryParse(element.getString("effect"));
		if (effectId==null) return null;
		RegistryEntry<StatusEffect> effectEntry=Registries.STATUS_EFFECT.getEntry(effectId).orElse(null);
		if (effectEntry==null) return null;
		StatusEffect effect=effectEntry.value();

		BoosterType bType=BoosterType.valueOf(element.getString("bType"));
		float value=element.getFloat("value");
		BoosterFor bFor=BoosterFor.valueOf(element.getString("bFor"));

		return new EffectBooster(effect, bType, value, bFor);
	}
}
