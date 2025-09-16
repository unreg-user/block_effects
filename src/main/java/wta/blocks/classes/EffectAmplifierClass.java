package wta.blocks.classes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wta.blocks.blockEntities.EffectAmplifierBEClass;

public class EffectAmplifierClass extends Block implements BlockEntityProvider {
	public EffectAmplifierClass(Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EffectAmplifierBEClass(pos, state);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		EffectAmplifierBEClass be=(EffectAmplifierBEClass) world.getBlockEntity(pos);
		if (be!=null)  be.onPlaced();
	}
}
