package wta.fun;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class BlockPosRandom {
	private static final long X_SALT = 0x9E3779B97F4A7C15L;
	private static final long Y_SALT = 0x3C6EF372FE94F82AL;
	private static final long Z_SALT = 0xA243A42932699F31L;

	public static Random of(ServerWorld world, BlockPos pos, long salt) {
		long seed = world.getSeed();
		seed += pos.getX() * X_SALT;
		seed += pos.getY() * Y_SALT;
		seed += pos.getZ() * Z_SALT;
		seed += salt;

		seed ^= seed >>> 31;
		seed *= 0x7FB8D355B3F28C43L;
		seed ^= seed >>> 27;
		seed *= 0x7FB8D355B3F28C43L;
		seed ^= seed >>> 31;

		return Random.create(seed);
	}
}
