package de.clayntech.dentahl4j.fx.api;

import de.clayntech.config4j.Config4J;
import de.clayntech.dentahl4j.api.NinjaServiceEndpoint;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.tooling.TaskManager;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FXNinjaService<T> extends NinjaServiceEndpoint {

    private Consumer<T> callback;
    public FXNinjaService(String baseUrl) {
        super(baseUrl);
    }

    public FXNinjaService(URL baseUrl) {
        super(baseUrl);
    }

    public FXNinjaService() {
        super(Config4J.getConfiguration().get(Keys.REST_BASE));
    }

    public <R> FXNinjaService<R> setCallback(Consumer<R> callback) {
        FXNinjaService<R> newService=new FXNinjaService<R>(getBaseUrl());
        newService.callback=callback;
        return newService;
    }

    private void callCallback(Object val) {
        if(callback!=null) {
            Platform.runLater(()->callback.accept((T)val));
        }
    }

    @Override
    public List<Ninja> getNinjaList() throws IOException {
        if(Platform.isFxApplicationThread()) {
            TaskManager man = TaskManager.getTaskManager();
            man.execute(()-> {
                try {
                    getNinjaList();
                } catch (IOException e) {
                    LOG.error("",e);
                }
            });
            return Collections.emptyList();
        }
        LOG.debug("Requesting the ninja list using: {}/list",getSafeURL());
        List<Ninja> list=super.getNinjaList();
        callCallback(list);
        return list;
    }
}
