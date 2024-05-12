package engine;

public interface Converter<T, V> {

	T convert(V value);
}
