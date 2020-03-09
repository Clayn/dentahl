package de.clayntech.dentahl4j.fx;

import java.io.File;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.Configuration;
import de.clayntech.config4j.ConfigurationProvider;
import de.clayntech.config4j.util.Config4JFileParser;
import de.clayntech.dentahl4j.fx.dialog.DDialogConfiguration;
import de.clayntech.dentahl4j.fx.dialog.DentahlDDialogConfiguration;
import de.clayntech.dentahl4j.fx.dialog.ErrorDDialog;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import jfxtras.styles.jmetro8.JMetro;
import kong.unirest.Unirest;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.fx.pre.D4JFXPreloader;

public class MainApp extends Application implements Thread.UncaughtExceptionHandler
{

    @Override
    public void init() throws Exception
    {
        Thread.setDefaultUncaughtExceptionHandler(this);
        Config4J.setProvider(ConfigurationProvider.newOSDependedFileProvider("Dentahl","dentahl.properties"));
        //disableSSLCertificateCheck();

        Config4J.importDefaultConfiguration(Config4JFileParser.loadConfiguration(getClass().getResourceAsStream("/dentahl.default.properties")));
        Config4J.saveConfiguration();
        I18n.getInstance().setLocale(Config4J.getConfiguration().get(Keys.LANGUAGE, Locale.ROOT));
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
            JMetro metro = new JMetro(JMetro.Style.DARK);
            metro.applyTheme(scene);
            metro.applyTheme(root);
            root.getStyleClass().add("background");
            stage.setTitle("Dentahl");
            stage.setScene(scene);
            Stage pre = new Stage();
            pre.getIcons().add(new Image(getClass().getResourceAsStream(
                    "/images/icon.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream(
                    "/images/icon.png")));
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
            loader.doWork(pre, metro);
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
                return new DentahlDDialogConfiguration(Alert.AlertType.ERROR,"error");
            }
        }.showAndWait();
    }
}
