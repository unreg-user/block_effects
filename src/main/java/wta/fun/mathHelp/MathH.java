package wta.fun.mathHelp;

import net.minecraft.block.Block;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Consumer;

public class MathH {
	public static boolean isInInterval(int interval, int pos){
		return pos<=interval && pos>=-interval;
	}
}
