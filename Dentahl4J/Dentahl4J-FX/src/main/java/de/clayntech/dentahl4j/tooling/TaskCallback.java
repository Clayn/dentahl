package de.clayntech.dentahl4j.tooling;

public interface TaskCallback<T> {

    void accept(T result);

    void onError(Exception ex);
}
