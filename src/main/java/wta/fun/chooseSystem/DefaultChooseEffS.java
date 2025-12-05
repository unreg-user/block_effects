package wta.fun.chooseSystem;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import wta.fun.BlockPosRandom;
import wta.gamerule.GamerulesInit;

import static wta.Block_effects.allEffectList;

public class DefaultChooseEffS extends ChooseEffS<StatusEffect, StatusEffectInstance, RegistryEntry<StatusEffect>>{
    private static final long ALL_SALT=18435L;
    private static final long MONSTER_SALT=65484L;
    private static final long NOT_MONSTER_SALT=31588L;

	private final Random random;
    private final Random worldRandom;
    private final ServerWorld world;

	public DefaultChooseEffS(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster) {
		super(difficulty, isMonster);
        this.world = world;
	    worldRandom = world.random;
		random = BlockPosRandom.of(world, pos, worldRandom.nextBoolean() ? ALL_SALT : (isMonster ? MONSTER_SALT : NOT_MONSTER_SALT));
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
					if (statusEffect.getCategory() == StatusEffectCategory.HARMFUL){
						statusEffect=allEffectList.get(random.nextInt(allEffectList.size()));
					}
				}
				case HARD -> {
					if (statusEffect.getCategory() != StatusEffectCategory.HARMFUL){
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

		return getInstanceDefaultRule(effectEntry, categoryAmplifier, world);
	}

    @Override
    public RegistryEntry<StatusEffect> getEntryStatusEffect() {
        return Registries.STATUS_EFFECT.getEntry(getStatusEffect());
    }
}
