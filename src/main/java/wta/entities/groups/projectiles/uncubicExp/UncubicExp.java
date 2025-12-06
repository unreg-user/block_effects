package wta.entities.groups.projectiles.uncubicExp;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import wta.entities.groups.projectiles.ProjectilesInit;
import wta.items.ItemsInit;

public class UncubicExp extends ProjectileEntity implements FlyingItemEntity {
    public static TrackedData<Boolean> IS_RED=DataTracker.registerData(UncubicExp.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final ItemStack RED_STACK=new ItemStack(ItemsInit.);

    public UncubicExp(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public UncubicExp(World world, Vec3d pos, Vec3d velocity, boolean isRed){
        super(ProjectilesInit.uncubicExpE, world);

        this.setPosition(pos);
        this.setVelocity(velocity);
        this.setIsRed(isRed);
    }

    public static UncubicExp registryCrate(EntityType<? extends ProjectileEntity> entityType, World world){
        return new UncubicExp(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(IS_RED, false);
    }

    public boolean getIsRed() {
        return dataTracker.get(IS_RED);
    }
    public void setIsRed(boolean value) {
        dataTracker.set(IS_RED, value);
    }

    @Override
    public ItemStack getStack() {
        return getIsRed() ? :;
    }
}
