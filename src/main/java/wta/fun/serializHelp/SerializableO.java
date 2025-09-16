package wta.fun.serializHelp;

public interface SerializableO<Y, T extends SerializableO<Y, T>>{
	Y serialize();
	default T deserializeInThis(Y value){throw new NotSerealizableInThis("Most likely this class is immutable.");}
}
