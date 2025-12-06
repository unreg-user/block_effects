package wta.items.classes;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wta.entities.groups.projectiles.uncubicExp.UncubicExp;
import wta.fun.directionHelp.DirectionH;

public class BlueExpItem extends AbstractExpItem{
    public BlueExpItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            int xpAmount = world.random.nextInt(5) + 3;
            double x = user.getX();
            double y = user.getEyeY() - 0.3;
            double z = user.getZ();

            for (int i = 0; i < xpAmount; i++) {
                ExperienceOrbEntity orb = new ExperienceOrbEntity(world, x, y, z, 1);
                orb.setVelocity(
                        (world.random.nextFloat() - 0.5) * 0.4,
                        world.random.nextFloat() * 0.3,
                        (world.random.nextFloat() - 0.5) * 0.4
                );
                world.spawnEntity(orb);
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return new UncubicExp(world, pos, DirectionH.dirToVec3d(direction), false);
    }
}
