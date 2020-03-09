package de.clayntech.dentahl4j.fx.dialog;

import de.clayntech.dentahl4j.fx.I18n;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

public abstract class DDialog {

    protected abstract DDialogConfiguration getConfiguration();

    protected Alert prepareAlert(DDialogConfiguration config) {
        Alert al=new Alert(config.getType());
        al.setGraphic(config.getGraphic());
        ResourceBundle bundle=ResourceBundle.getBundle(config.getBundleBase(), I18n.getInstance().getLocale());
        if(config.isShowTitle()) {
            al.setTitle(bundle.getString(config.getKeyBase() +".title"));
        }
        if(config.isShowHeader()) {
            al.setHeaderText(bundle.getString(config.getKeyBase() +".header"));
        }else {
            al.setHeaderText(null);
        }
        if(config.isShowMessage()) {
            al.setContentText(bundle.getString(config.getKeyBase() +".message"));
        }
        else {
            al.setContentText(null);
        }
        config.configure(al);
        return al;
    }

    public void show() {
        prepareAlert(getConfiguration()).show();
    }

    public Optional<ButtonType> showAndWait() {
        return prepareAlert(getConfiguration()).showAndWait();
    }


}
