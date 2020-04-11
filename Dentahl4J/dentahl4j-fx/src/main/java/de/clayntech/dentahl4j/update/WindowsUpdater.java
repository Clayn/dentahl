package de.clayntech.dentahl4j.update;

import de.clayntech.dentahl4j.config.OS;
import de.clayntech.dentahl4j.config.Version;
import de.clayntech.dentahl4j.fx.I18n;
import de.clayntech.dentahl4j.fx.util.MessagedProgressListener;
import de.clayntech.dentahl4j.fx.util.ProgressListener;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class WindowsUpdater implements Updater {
    private static final Logger LOG= LoggerFactory.getLogger(WindowsUpdater.class);
    private static final String UPDATE_BASE="http://www.clayncraft.de/dentahl/";
    private static final String BINARY_BASE=UPDATE_BASE+"binaries/";
    private static final String VERSION_FILE=UPDATE_BASE+"versions.properties";
    private String remoteVersion=null;
    private boolean loadable=true;
    private final Properties versionsProperties=new Properties();

    private static final class ProgressingInputStream extends FilterInputStream {

        private final MessagedProgressListener listener;
        private final long maxSize;
        private long read=0;
        private double progress=-1;
        protected ProgressingInputStream(InputStream in, MessagedProgressListener listener, long max) {
            super(in);
            this.maxSize=max;
            this.listener=listener;
            listener.progressChanged(progress);
        }

        private void calcProgress() {
            progress=(read*1.0)/(maxSize*1.0);
            listener.progressChanged(progress);
        }

        @Override
        public int read() throws IOException {
            int r=super.read();
                    read+=r;
                    calcProgress();
                    return r;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int r=super.read(b);
            read+=r;
            calcProgress();
            return r;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int r=super.read(b,off,len);
            read+=r;
            calcProgress();
            return r;
        }
    }

    private void loadRemoteVersion() {
        if(remoteVersion!=null) {
            return;
        }
        if(!loadable) {
            throw new RuntimeException("Can't access the update resources");
        }
        LOG.debug("Loading version properties from {}",VERSION_FILE);
        try(InputStream in=new URL(VERSION_FILE).openStream()) {
            versionsProperties.load(in);
            remoteVersion=versionsProperties.getProperty("current");
        } catch (IOException e) {
            LOG.error("Failed to get the versions file",e);
            loadable=false;
        }
    }
    private int toVersionNumber(String ver) {
        String[] split=ver.split("\\.");
        List<String> parts=new ArrayList<>(Arrays.asList(split));
        Collections.reverse(parts);
        int res=0;
        for(int i=0;i<parts.size();++i) {
            res+=Math.pow(10,i)*Integer.parseInt(parts.get(i));
        }
        return res;
    }
    public boolean isUpdateAvailable() {
        loadRemoteVersion();
        int localVersion=toVersionNumber(Version.getVersion());
        int remVersion=toVersionNumber(remoteVersion);
        return remVersion>localVersion;
    }

    private long getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public void doUpdate(ProgressListener listener) throws IOException {
        if(!isUpdateAvailable()) {
            return;
        }
        Path tmpDir= OS.getOSDirectory("tmp").toPath();
        Files.createDirectories(tmpDir);
        Path tmpFile=new File(tmpDir.toFile(),"update.msi").toPath();
        Files.createFile(tmpFile);
        String url=BINARY_BASE+versionsProperties.getProperty("version."+remoteVersion);
        LOG.debug("Loading version {} from {} to {}",remoteVersion,url,tmpFile.toAbsolutePath());
        URL u=new URL(url);
        long fileSize=getFileSize(u);
        try(InputStream in=new ProgressingInputStream(u.openStream(), new MessagedProgressListener(String.format(I18n.getInstance().getBundle().getString("update.message"),getRemoteVersion())) {
            @Override
            public void progressChanged(String message, double progress) {
                listener.progressChanged(message,progress);
            }
        },fileSize)) {
            Files.copy(in,tmpFile, StandardCopyOption.REPLACE_EXISTING);
        }
        ProcessBuilder builder=new ProcessBuilder()
                .directory(tmpDir.toFile())

                .command("msiexec","/i",tmpFile.toAbsolutePath().toString());
        LOG.debug("Starting update file: {}",tmpFile.toAbsolutePath().toString());
        builder.start();
        Platform.exit();
        System.exit(0);
    }

    public String getRemoteVersion() {
        loadRemoteVersion();
        return remoteVersion;
    }
}
