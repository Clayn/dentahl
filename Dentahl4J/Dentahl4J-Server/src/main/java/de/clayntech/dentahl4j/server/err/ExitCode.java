package de.clayntech.dentahl4j.server.err;

public interface ExitCode {
    int SERVER_ALREADY_RUNNING=10;
    int DATA_MISSING=11;
    int APPLICATION_PROPERTIES_MISSNG=12;
    int FILES_MISSING=13;
}
