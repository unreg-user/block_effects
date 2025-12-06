package wta.fun.directionHelp;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.RedstoneView.DIRECTIONS;

public class DirectionH {
    private static final Vec3d[] dirToVec3d=Arrays.stream(Direction.values())
            .map(dir -> new Vec3d(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()))
            .toArray(Vec3d[]::new);

    public static Vec3d dirToVec3d(Direction direction){
        return dirToVec3d[direction.ordinal()];
    }
}
