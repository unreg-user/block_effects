package wta.fun.mathHelp;

import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.function.Consumer;

public class RandomH {
	public static <T> T getRandom(Random random, int sumWeights, int[] listWeights, T[] all_types){
		int pos=random.nextInt(sumWeights);
		int posIStop=0;
		for (int i=0; i<listWeights.length; i++) {
			int weightI=listWeights[i];
			pos-=weightI;
			if (pos<0){
				posIStop=i;
				break;
			}
		}
		return all_types[posIStop];
	}

	public static <T> T getRandom(Random random, T[] values){
		return values[random.nextInt(values.length)];
	}

	public static <Y, T extends List<Y>> Y getRandom(Random random, T values){
		return values.get(random.nextInt(values.size()));
	}

	public static <Y, T extends List<Y>> Y getRandomAndDelete(Random random, T values){
		int pos=random.nextInt(values.size());
		Y ret=values.get(pos);
		values.remove(pos);
		return ret;
	}

	@Contract(mutates = "param3")
	public static <T> void forGroupRoundRandom(Random random, int count, List<T> list, Consumer<T> consumer){
		int interval=list.size();
		while (count>0){
			if (count>=interval){
				for (T i : list){
					consumer.accept(i);
				}
				count-=interval;
			}else{
				while (count>0){
					consumer.accept(getRandomAndDelete(random, list));
					count--;
				}
			}
		}
	}
}
