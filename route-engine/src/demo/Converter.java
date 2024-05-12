package demo;

public interface Converter<T, V> {

	T convert(V value);
}
