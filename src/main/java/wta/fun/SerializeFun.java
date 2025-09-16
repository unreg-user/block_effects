package wta.fun;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import wta.fun.serializHelp.SerializableO;

import java.util.List;
import java.util.Objects;

public class SerializeFun {
	public static <Y extends NbtElement, T extends SerializableO<Y, T>> NbtList serList(List<T> list){
		list.stream().map(SerializableO::serialize).filter(Objects::nonNull)
	}
}
