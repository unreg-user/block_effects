package wta.fun.serializHelp;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.List;
import java.util.Objects;

public class SerializeFun {
	public static <Y extends NbtElement, T extends SerializableO<Y, T>> NbtList serList(List<T> list, byte type){
		List<NbtElement> serList=list.stream()
			  .map(SerializableO::serialize)
			  .filter(Objects::nonNull)
			  .map(el -> (NbtElement) el)
			  .toList();
		return new NbtList(serList, type);
	}
	public static <Y extends NbtCompound, T extends SerializableO<Y, T>> NbtList serList(List<T> list){
		return serList(list, NbtList.COMPOUND_TYPE);
	}
}
