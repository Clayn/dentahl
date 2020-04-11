package de.clayntech.dentahl4j.update;

import de.clayntech.dentahl4j.fx.util.ProgressListener;

public interface Updater {

    String getRemoteVersion()throws Exception;

    boolean isUpdateAvailable()throws Exception;

    void doUpdate(ProgressListener listener) throws Exception;
}
