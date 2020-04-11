package de.clayntech.dentahl4j.fx.dialog;

import de.clayntech.dentahl4j.config.OS;
import de.clayntech.dentahl4j.config.Version;
import de.clayntech.dentahl4j.fx.util.DentahlController;
import de.clayntech.dentahl4j.fx.util.ProgressListener;
import de.clayntech.dentahl4j.update.Updater;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateDialogController extends DentahlController implements ProgressListener {

    private final BooleanProperty working=new SimpleBooleanProperty(false);
    private final BooleanProperty input=new SimpleBooleanProperty(false);
    @FXML
    public Button checkButton;
    @FXML
    public Button updateButton;
    @FXML
    public Label versionLabel;
    @FXML
    public VBox progressBox;
    @FXML
    public Label messageLabel;
    @FXML
    public ProgressBar updateProgress;
    private final Updater updater= OS.getUpdater();
    @FXML
    public Button closeButton;

    @Override
    protected void onInit() {
        progressBox.visibleProperty().bind(working);
        checkButton.visibleProperty().bind(working.not().and(input.not()));
        updateButton.visibleProperty().bind(working.not().and(input));
        versionLabel.setText(Version.getVersion());
    }

    private void setState(BooleanProperty prop, boolean state) {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(()->setState(prop,state));
            return;
        }
        prop.set(state);
    }
    public void onClose(ActionEvent actionEvent) {
        progressBox.getScene().getWindow().hide();
    }

    public void doUpdate(ActionEvent actionEvent) {
        UpdateDialogController self=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setState(closeButton.disableProperty(),true);
                    setState(working,true);
                    updater.doUpdate(self);
                } catch (Exception e) {
                    setState(closeButton.disableProperty(),false);
                    LOG.error("Failed to do the update",e);
                }
            }
        },"Dentahl-Updater").start();
    }

    public void doCheck(ActionEvent actionEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setState(input,false);
                    setState(working,true);
                    progressChanged(resources.getString("update.check"),-1);
                    boolean avail=updater.isUpdateAvailable();
                    if(avail) {
                        setState(working,false);
                        setState(input,true);
                    }else {
                        progressChanged(resources.getString("update.none"),0.0);
                    }
                } catch (Exception e) {
                    LOG.error("Failed to check for updates",e);
                }
            }
        },"Update-Check").start();
    }

    @Override
    public void progressChanged(String message, double progress) {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(()->progressChanged(message,progress));
            return;
        }
        messageLabel.setText(message);
        updateProgress.setProgress(progress);
    }
}
