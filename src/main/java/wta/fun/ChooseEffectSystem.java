package wta.fun;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import wta.gamerule.GamerulesInit;

import java.util.ArrayList;

import static wta.Block_effects.allEffectList;

public class ChooseEffectSystem {
	public StatusEffect effect;
	private Random random;

	public ChooseEffectSystem getFor(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster){
		random=BlockPosRandom.of(world, pos, 18435L);
		ArrayList<StatusEffect> rule=world.getGameRules().get(GamerulesInit.BlockDisabledEffectsGR).getTValue();
		ArrayList<StatusEffect> effects=new ArrayList<>(allEffectList.stream()
			  .filter(rule::contains)
			  .toList());

		StatusEffect statusEffect=effects.get(random.nextInt(effects.size()));

		for (int i = 0; i < switch (difficulty){
			case HARD, PEACEFUL -> 2;
			case NORMAL -> 0;
			case EASY -> 1;
		}; i++) {
			switch (difficulty){
				case PEACEFUL, EASY -> {
					if (statusEffect.getCategory()== StatusEffectCategory.HARMFUL){
						statusEffect=effects.get(random.nextInt(effects.size()));
					}
				}
				case HARD -> {
					if (statusEffect.getCategory()!=StatusEffectCategory.HARMFUL){
						statusEffect=effects.get(random.nextInt(effects.size()));
					}
				}
			}
		}

		return this;

		RegistryEntry<StatusEffect> effect= Registries.STATUS_EFFECT.getEntry(statusEffect);

		StatusEffectCategory category=statusEffect.getCategory();
		int categoryAmplifier = getCategoryAmplifier(isMonster, difficulty, category);

		return new StatusEffectInstance(
					effect,
					120,
					categoryAmplifier-1
			  );
	}

	public void andToInstance(){

	}

	private static int getCategoryAmplifier(boolean isMonster, Difficulty difficulty, StatusEffectCategory category) {
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
