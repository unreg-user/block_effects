package wta.fun.chooseSystem;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.Difficulty;

public abstract class ChooseEffS {
	protected final Difficulty difficulty;
	protected final boolean isMonster;

	public ChooseEffS(Difficulty difficulty, boolean isMonster) {
		this.difficulty = difficulty;
		this.isMonster = isMonster;
	}

	public RegistryEntry<StatusEffect> getEntryStatusEffect(){
		return Registries.STATUS_EFFECT.getEntry(getStatusEffect());
	}

	public abstract StatusEffect getStatusEffect();
	public abstract StatusEffectInstance getStatusEffectInstance();

	protected static int getCategoryAmplifier(boolean isMonster, Difficulty difficulty, StatusEffectCategory category) {
		int categoryAmplifier;

		if (isMonster){
			categoryAmplifier=switch (difficulty){
				case EASY -> switch (category){
					case HARMFUL -> 4;
					case NEUTRAL -> 2;
					case BENEFICIAL -> 1;
				};
				case NORMAL -> switch (category){
					case HARMFUL -> 1;
					case NEUTRAL -> 3;
					case BENEFICIAL -> 4;
				};
				case HARD -> switch (category){
					case HARMFUL -> 1;
					case NEUTRAL -> 3;
					case BENEFICIAL -> 5;
				};
				default -> switch (category){
					case HARMFUL -> 5;
					case NEUTRAL -> 2;
					case BENEFICIAL -> 1;
				};
			};
		}else{
			categoryAmplifier=switch (difficulty){
				case EASY -> switch (category){
					case HARMFUL -> 1;
					case NEUTRAL -> 3;
					case BENEFICIAL -> 2;
				};
				case NORMAL -> switch (category){
					case HARMFUL -> 4;
					case NEUTRAL -> 2;
					case BENEFICIAL -> 1;
				};
				case HARD -> switch (category){
					case HARMFUL -> 5;
					case NEUTRAL -> 2;
					case BENEFICIAL -> 1;
				};
				default -> switch (category){
					case HARMFUL -> 1;
					case NEUTRAL -> 2;
					case BENEFICIAL -> 3;
				};
			};
		}

		return categoryAmplifier;
	}
}
