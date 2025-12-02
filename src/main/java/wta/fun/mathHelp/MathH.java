package wta.fun.mathHelp;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MathH {
	public static boolean isInInterval(int interval, int pos){
		return pos<=interval && pos>=-interval;
	}

	public static int getSqDist(int x, int y, int z){
		return x*x + y*y + z*z;
	}

	public static Text getPercent(float value){
		int value2 = (int) ((value-1)*100);
		if (value2 >= 0){
			return Text.literal("+"+value2+"%").styled(style -> style.withColor(Formatting.GREEN));
		}else{
			return Text.literal(value2+"%").styled(style -> style.withColor(Formatting.RED));
		}
	}
}
