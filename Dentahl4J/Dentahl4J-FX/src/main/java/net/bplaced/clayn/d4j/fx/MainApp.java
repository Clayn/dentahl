package net.bplaced.clayn.d4j.fx;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import net.bplaced.clayn.d4j.config.DentahlConfiguration;
import net.bplaced.clayn.d4j.config.Keys;
import net.bplaced.clayn.d4j.fx.pre.D4JFXPreloader;

public class MainApp extends Application
{

    @Override
    public void init() throws Exception
    {
        //disableSSLCertificateCheck();
        DentahlConfiguration.getConfiguration().load();
        if (!DentahlConfiguration.getConfiguration().isSet(Keys.REST_BASE))
        {
            DentahlConfiguration.getConfiguration().set(Keys.REST_BASE, new URL(
                    "http://localhost:8080/dentahl/v2/"));
            DentahlConfiguration.getConfiguration().store();
        }
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
        loader.finishedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    pre.hide();
                    stage.show();
                }
            }
        });
        stage.setOnCloseRequest((evt) -> Unirest.shutDown());
        loader.doWork(pre, metro);
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

}
