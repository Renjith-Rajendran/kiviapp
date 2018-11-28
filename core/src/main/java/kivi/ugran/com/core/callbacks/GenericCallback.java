package kivi.ugran.com.core.callbacks;

public interface GenericCallback<T> {
    void success(T response);
    void error(String err);
}
