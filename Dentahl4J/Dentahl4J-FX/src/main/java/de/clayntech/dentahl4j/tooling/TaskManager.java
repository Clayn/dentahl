package de.clayntech.dentahl4j.tooling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TaskManager implements Closeable {

    private static TaskManager INSTANCE=new TaskManager();
    private final ExecutorService executorService= Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t=new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if(executorService.isShutdown()||executorService.isTerminated()) {
                   return;
                }
                try {
                    LOG.info("Shutting down the task manager");
                    close();
                } catch (IOException e) {
                    LOG.error("Error while shutting down the task manager",e);
                }
            }
        },"Taskmanager-Shutdown-Hook"));
    }
    public static TaskManager getTaskManager() {
        return INSTANCE;
    }
    private static final Logger LOG= LoggerFactory.getLogger(TaskManager.class);
    private static final TaskCallback<Object> DEFAULT_CALLBACK=new TaskCallback<Object>() {
        @Override
        public void accept(Object result) {
            LOG.info("Default Callback got a result");
        }

        @Override
        public void onError(Exception ex) {
            LOG.error("",ex);
        }
    };
    private final Map<String,ApplicationTask<?>> registeredTasks=new HashMap<>();

    public void registerTask(String name,ApplicationTask<?> task) {
        synchronized (registeredTasks) {
            if (registeredTasks.containsKey(name)) {
                throw new IllegalArgumentException("Task '" + name + "' already exists");
            }
            registeredTasks.put(name, task);
        }
    }

    public void callTask(String name) {
        callTask(name,DEFAULT_CALLBACK);
    }

    public <T> void callTask(String name, TaskCallback<T> callback) {
        synchronized (registeredTasks) {
            if (!registeredTasks.containsKey(name)) {
                throw new IllegalArgumentException("No task registered for name '" + name + "'");
            }
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("Task-"+name);
                try{
                    ApplicationTask<?> task=registeredTasks.get(name);
                    Object val=registeredTasks.get(name).execute();
                    T result=null;
                    try{
                        result= (T) val;
                    }catch (Exception ex) {
                        throw new RuntimeException("Result type is not matching the callback type");
                    }

                    if(callback!=null) {
                        callback.accept(result);
                    }else if(result!=null){
                        LOG.info("No callback was registered for a calculated result");
                    }
                }catch (Exception ex) {
                    if(callback!=null) {
                        callback.onError(ex);
                    }else {
                        LOG.error("Error while executing task '{}'",name,ex);
                    }
                }
            }
        });

    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
        executorService.shutdownNow();
    }

    public void execute(Runnable run,String name) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName(name);
                run.run();
            }
        });
    }

    public void execute(Runnable run) {
        execute(run,"TaskManager-Daemon");
    }
}
