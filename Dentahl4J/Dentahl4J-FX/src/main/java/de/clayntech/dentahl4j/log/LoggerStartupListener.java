package de.clayntech.dentahl4j.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.io.File;

public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    public final String getLogDirectory() {
        return new File(
                this.windows ? System.getenv().getOrDefault("APPDATA", System.getProperty("user.home")) : System.getProperty("user.home"), "Dentahl4J/logs"
        ).getAbsolutePath();
    }

    private boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
    private boolean started = false;

    @Override
    public void start() {
        if (this.started) {
            return;
        }
        Context context = this.getContext();

        context.putProperty("LOG_DIR", this.getLogDirectory());

        this.started = true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public boolean isResetResistant() {
        return true;
    }

    @Override
    public void onStart(final LoggerContext context) {
    }

    @Override
    public void onReset(final LoggerContext context) {
    }

    @Override
    public void onStop(final LoggerContext context) {
    }

    @Override
    public void onLevelChange(final Logger logger, final Level level) {
    }
}