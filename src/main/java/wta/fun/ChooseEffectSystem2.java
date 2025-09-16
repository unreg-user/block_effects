package wta.fun;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import wta.blocks.BlocksInit;

import java.util.ArrayList;

public class ChooseEffectSystem2 extends ChooseEffectSystem{
	public static final int radiusE=2;
	public static final int radiusEE=3;
	public static final int radiusUE=4;

	private final BlockPos pos;
	private final ServerWorld world;
	private BlockPos posN;
	private ArrayList<> effectAmplifiers;

	public ChooseEffectSystem2(ServerWorld world, Difficulty difficulty, BlockPos pos, boolean isMonster) {
		super(world, difficulty, pos, isMonster);
		this.world=world;
		this.pos=pos;
		this.posN=pos;
	}

	@Override
	public RegistryEntry<StatusEffect> getEntryStatusEffect() {
		ArrayList<BlockPos> radius=new ArrayList<>();
		ArrayList<BlockPos> radiusU=new ArrayList<>();
		for (int ix = -radiusUE; ix <= radiusUE; ix++) {
			for (int iy = -radiusUE; iy <= radiusUE; iy++) {
				for (int iz = -radiusUE; iz <= radiusUE; iz++) {
					BlockPos posI=pos.add(ix, iy, iz);
					Block block=world.getBlockState(posI).getBlock();
					if (block==BlocksInit.radiusEffectB){

					} else if (block==BlocksInit.radiusEffectAmplifierB) {

					}
				}
			}
		}
		return super.getEntryStatusEffect();
	}
}
