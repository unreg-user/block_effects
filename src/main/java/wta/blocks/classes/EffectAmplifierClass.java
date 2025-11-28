package wta.blocks.classes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
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
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		EffectAmplifierBEClass be=(EffectAmplifierBEClass) world.getBlockEntity(pos);

		if (world.isClient) return ActionResult.FAIL;

		if (be!=null) {
			return be.onPlaced(player) ? ActionResult.SUCCESS : ActionResult.FAIL;
		}else{
			return ActionResult.PASS;
		}
	}
}
