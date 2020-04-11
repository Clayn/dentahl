package de.clayntech.dentahl4j.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class IO {
    private static final Logger LOG= LoggerFactory.getLogger(IO.class);
    public static void deleteDirectory(Path f) throws IOException {
        if(!Files.exists(f)) {
            return;
        }
        if(Files.isRegularFile(f)) {
            deleteFile(f);
            return;
        }
        clearDirectory(f);
        LOG.debug("Deleting: {}",f);
        Files.delete(f);
    }

    public static void clearDirectory(Path f) throws IOException {
        if(!Files.exists(f)) {
            return;
        }
        if(Files.isRegularFile(f)) {
            deleteFile(f);
            return;
        }
        Files.list(f)
                .forEach(new Consumer<Path>() {
                    @Override
                    public void accept(Path path) {
                        try {
                            deleteDirectory(path);
                        } catch (IOException e) {
                            LOG.error("Failed to delete: {}",path,e);
                        }
                    }
                });
    }

    public static void deleteFile(Path f) throws IOException {
        if(!Files.exists(f)) {
            return;
        }
        if(Files.isDirectory(f)) {
            deleteFile(f);
            return;
        }LOG.debug("Deleting: {}",f);
        Files.deleteIfExists(f);
    }
}
