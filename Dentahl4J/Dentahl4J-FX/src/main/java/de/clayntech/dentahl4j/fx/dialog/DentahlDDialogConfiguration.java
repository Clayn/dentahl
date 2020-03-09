package de.clayntech.dentahl4j.fx.dialog;

import javafx.scene.Node;
import javafx.scene.control.Alert;

public class DentahlDDialogConfiguration extends DDialogConfiguration {
    public DentahlDDialogConfiguration(Alert.AlertType type,String keyBase) {
        super(type, keyBase, "i18n.dialog", null, true, false, true);
    }
}
