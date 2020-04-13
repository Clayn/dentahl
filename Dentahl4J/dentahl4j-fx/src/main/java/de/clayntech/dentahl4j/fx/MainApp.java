package de.clayntech.dentahl4j.fx;

import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.ConfigurationProvider;
import de.clayntech.config4j.rt.Runtime4J;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.config.OS;
import de.clayntech.dentahl4j.fx.dialog.DDialogConfiguration;
import de.clayntech.dentahl4j.fx.dialog.DentahlDDialogConfiguration;
import de.clayntech.dentahl4j.fx.dialog.ErrorDDialog;
import de.clayntech.dentahl4j.fx.pre.D4JFXPreloader;
import de.clayntech.dentahl4j.fx.util.ProgressListener;
import de.clayntech.dentahl4j.fx.util.UIUtils;
import de.clayntech.dentahl4j.io.IO;
import de.clayntech.dentahl4j.tooling.TaskManager;
import de.clayntech.dentahl4j.update.WindowsUpdater;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Locale;

public class MainApp extends Application implements Thread.UncaughtExceptionHandler
{
    private static final Logger LOG= LoggerFactory.getLogger(MainApp.class);

    @Override
    public void init() throws Exception
    {
        LOG.debug("Starting up the application at {}",System.getProperty("user.dir"));
        File f=new File(System.getProperty("user.dir"));
        if(Files.isWritable(f.toPath())) {
            LOG.debug("User directory can be used");
        }else {
            String newDir= OS.getOSUserHome();
            LOG.debug("User directory can't be used. Change to {}",newDir);
            System.setProperty("user.dir",newDir);
        }
        LOG.debug("Starting the Task manager");
        Runtime4J.getRuntime().reserveType(Keys.TASK_MANAGER, TaskManager.class);
        Runtime4J.getRuntime().setObject(Keys.TASK_MANAGER, TaskManager.getTaskManager());
        //Thread.setDefaultUncaughtExceptionHandler(this);
        Config4J.setProvider(ConfigurationProvider.newOSDependedFileProvider("Dentahl4J","dentahl.properties"));
        //disableSSLCertificateCheck();
        //Config4J.importDefaultConfiguration(Config4JFileParser.loadConfiguration(getClass().getResourceAsStream("/dentahl.default.properties")));
        Config4J.getConfiguration().set(Keys.REST_BASE,new URL("http://www.clayncraft.de:10000/dentahl/v2/"));
        Config4J.saveConfiguration();
        I18n.getInstance().setLocale(Config4J.getConfiguration().get(Keys.LANGUAGE, Locale.ROOT));
        LOG.debug("Clearing the temporary directory");
        File tmpDir=OS.getOSDirectory("Temp");
        IO.clearDirectory(tmpDir.toPath());
    }

    private void disableSSLCertificateCheck() throws NoSuchAlgorithmException, KeyManagementException
    {
        TrustManager[] trustAllCerts = new TrustManager[]
        {
            new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType)
                {
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier()
        {
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        try {

            FXMLLoader windowLoader = new FXMLLoader(getClass().getResource(
                    "/fxml/MainWindow.fxml"));
            windowLoader.setResources(I18n.getInstance().getBundle());
            Parent root = windowLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");

            JMetro metro = new JMetro(scene, Style.DARK);
            root.getStyleClass().add("background");
            stage.setTitle("Dentahl");
            stage.setScene(scene);
            Stage pre = new Stage();
            UIUtils.setStageIcon(pre);
            UIUtils.setStageIcon(stage);
            pre.setTitle("Dentahl Preloader");
            D4JFXPreloader loader = new D4JFXPreloader();
            loader.finishedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(
                        ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        pre.hide();
                        stage.show();
                    }
                }
            });
            stage.setOnCloseRequest((evt) -> Unirest.shutDown());
            System.out.println("Showing the preloader");
            loader.doWork(pre, metro,windowLoader.getResources());

        }catch (Exception e) {
            // We want to handle the exceptions with out handler first to show an error dialog while starting the application
            uncaughtException(Thread.currentThread(),e);
            throw e;
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        new ErrorDDialog(e){

            @Override
            protected DDialogConfiguration getConfiguration() {
                try {
                    return new DentahlDDialogConfiguration(Alert.AlertType.ERROR, "error");
                }catch (Exception ex) {
                    LOG.error("Failed to show the error dialog");
                    e.printStackTrace();
                    return null;
                }
            }
        }.showAndWait();
    }
}
