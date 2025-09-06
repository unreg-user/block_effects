package wta.mixins.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.fun.BlockPosRandom;
import wta.gamerule.GamerulesInit;

import java.util.ArrayList;
import java.util.Map;

import static wta.Block_effects.allEffectList;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFixer extends Entity {
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Unique
	private int tickCounter=0;

	@Inject(
		  method = "tick",
		  at = @At("RETURN")
	)
	private void tick_(CallbackInfo ci){
		World world_=this.getWorld();
		if (world_.isClient) return;
		ServerWorld world=(ServerWorld) world_;
		Difficulty difficulty=world.getDifficulty();

		tickCounter--;
		if (tickCounter<=0){
			tickCounter=140;

			Random random=BlockPosRandom.of(world, this.getBlockPos(), 18435L);
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
						if (statusEffect.getCategory()==StatusEffectCategory.HARMFUL){
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

			RegistryEntry<StatusEffect> effect=Registries.STATUS_EFFECT.getEntry(statusEffect);

			StatusEffectCategory category=statusEffect.getCategory();
			boolean isMonster=this instanceof Monster;
			int categoryAmplifier = getCategoryAmplifier(isMonster, difficulty, category);

			this.addStatusEffect(
				  new StatusEffectInstance(
						effect,
					    120,
					    categoryAmplifier-1
				  )
			);
		}
	}

	@Inject(
		  method = "<init>",
		  at = @At("RETURN")
	)
	private void q(EntityType<? extends LivingEntity> entityType, World world, CallbackInfo ci){
		if (allEffectList==null){
			allEffectList=Registries.STATUS_EFFECT.getEntrySet().stream().map(Map.Entry::getValue).toList();
		}
	}

	//костыли
	public LivingEntityFixer(EntityType<?> type, World world) {super(type, world);}
}
