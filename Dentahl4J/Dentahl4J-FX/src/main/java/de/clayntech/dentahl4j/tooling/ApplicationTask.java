package de.clayntech.dentahl4j.tooling;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface ApplicationTask<T> extends Callable<T> {

    T execute() throws Exception;

    @Override
    default T call() throws Exception {
        return execute();
    }
}
