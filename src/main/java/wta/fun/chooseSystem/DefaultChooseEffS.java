package wta.fun.chooseSystem;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import wta.fun.BlockPosRandom;

import static wta.Block_effects.allEffectList;

public class DefaultChooseEffS extends ChooseEffS{
	protected final Random random;

	public DefaultChooseEffS(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster) {
		super(difficulty, isMonster);
		random=BlockPosRandom.of(world, pos, 18435L);
	}

	@Override
	public StatusEffect getStatusEffect(){
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

		return statusEffect;
	}

	@Override
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
}
