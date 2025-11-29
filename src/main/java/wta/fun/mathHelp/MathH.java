package wta.fun.mathHelp;

public class MathH {
	public static boolean isInInterval(int interval, int pos){
		return pos<=interval && pos>=-interval;
	}
	public static int getSqDist(int x, int y, int z){
		return x*x + y*y + z*z;
	}
}
