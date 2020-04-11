package de.clayntech.dentahl4j.fx.util;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class DentahlController implements Initializable {
    protected final Logger LOG= LoggerFactory.getLogger(getClass());

    protected ResourceBundle resources;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources=resourceBundle;
        onInit();
    }

    protected abstract void onInit();
}
