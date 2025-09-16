package wta.fun;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;

import static wta.Block_effects.allEffectList;

public class ChooseEffectSystem {
	protected final Random random;

	protected final Difficulty difficulty;
	protected final boolean isMonster;

	public ChooseEffectSystem(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster) {
		this.difficulty = difficulty;
		this.isMonster = isMonster;

		random=BlockPosRandom.of(world, pos, 18435L);
	}

	public RegistryEntry<StatusEffect> getEntryStatusEffect(){
		StatusEffect statusEffect=allEffectList.get(random.nextInt(allEffectList.size()));

		for (int i = 0; i < switch (difficulty){
			case HARD, PEACEFUL -> 2;
			case NORMAL -> 0;
			case EASY -> 1;
		}; i++) {
			switch (difficulty){
				case PEACEFUL, EASY -> {
					if (statusEffect.getCategory()== StatusEffectCategory.HARMFUL){
						statusEffect=allEffectList.get(random.nextInt(allEffectList.size()));
					}
				}
				case HARD -> {
					if (statusEffect.getCategory()!=StatusEffectCategory.HARMFUL){
						statusEffect=allEffectList.get(random.nextInt(allEffectList.size()));
					}
				}
			}
		}

		return Registries.STATUS_EFFECT.getEntry(statusEffect);
	}

	public StatusEffectInstance getStatusEffectInstance(){
		RegistryEntry<StatusEffect> effectEntry=getEntryStatusEffect();
		StatusEffect effect=effectEntry.value();
		StatusEffectCategory category=effect.getCategory();

		int categoryAmplifier = getCategoryAmplifier(isMonster, difficulty, category);

		return new StatusEffectInstance(
			  effectEntry,
			  120,
			  categoryAmplifier-1
		);
	}

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
