package wta.items.classes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import wta.Block_effects;
import wta.fun.chooseSystem.ChooseEffS;

import static wta.Block_effects.allEffectList;

public abstract class AbstractExpItem extends Item implements ProjectileItem {
    public AbstractExpItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.BLOCK_SCULK_HIT;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient){
            ServerWorld serverWorld=(ServerWorld) world;
            StatusEffect effect=getEffect(stack, world, user);
            RegistryEntry<StatusEffect> effectEntry=Registries.STATUS_EFFECT.getEntry(effect);

            user.addStatusEffect(ChooseEffS.getInstanceDefaultRule(
                    effectEntry,
                    ChooseEffS.getCategoryAmplifier(user instanceof Monster, world.getDifficulty(), effect.getCategory()),
                    serverWorld
            ));
        }

        return super.finishUsing(stack, world, user);
    }

    protected StatusEffect getEffect(ItemStack stack, World world, LivingEntity user){
        return allEffectList.get(world.random.nextInt(allEffectList.size()));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.)
        return super.use(world, user, hand);
    }
}
