package wta.mixins.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wta.Block_effects;
import wta.fun.ChooseEffectSystem;

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

			this.addStatusEffect(
				  new ChooseEffectSystem(world, difficulty, this.getBlockPos(), this instanceof Monster)
						.getStatusEffectInstance()
			);
		}
	}

	@Inject(
		  method = "<init>",
		  at = @At("RETURN")
	)
	private void reInit(EntityType<?> entityType, World world, CallbackInfo ci){
		if (Block_effects.allEffectList==null && !world.isClient){
			Block_effects.reInitEffects((ServerWorld) world);
		}
	}

	//костыли
	public LivingEntityFixer(EntityType<?> type, World world) {super(type, world);}
}
