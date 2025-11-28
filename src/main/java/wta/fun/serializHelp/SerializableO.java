package wta.fun.serializHelp;

public interface SerializableO<Y, T extends SerializableO<Y, T>>{
	Y serialize();
	default T deserializeIntoThis(Y value){
		throw new CannotSerializeIntoThisException("Most likely this class is immutable.");
	}
}
