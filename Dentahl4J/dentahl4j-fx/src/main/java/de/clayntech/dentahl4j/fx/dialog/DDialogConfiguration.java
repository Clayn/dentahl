package de.clayntech.dentahl4j.fx.dialog;

import javafx.scene.Node;
import javafx.scene.control.Alert;

public class DDialogConfiguration {
    private final Alert.AlertType type;
    private final String keyBase;
    private final String bundleBase;
    private final Node graphic;
    private final boolean showTitle;
    private final boolean showHeader;
    private final boolean showMessage;

    public DDialogConfiguration(Alert.AlertType type, String keyBase, String bundleBase, Node graphic, boolean showTitle, boolean showHeader, boolean showMessage) {
        this.type = type;
        this.keyBase = keyBase;
        this.bundleBase = bundleBase;
        this.graphic = graphic;
        this.showTitle = showTitle;
        this.showHeader = showHeader;
        this.showMessage = showMessage;
    }

    public Alert.AlertType getType() {
        return type;
    }

    public String getKeyBase() {
        return keyBase;
    }

    public String getBundleBase() {
        return bundleBase;
    }

    public Node getGraphic() {
        return graphic;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    protected void configure(Alert al) {

    }
}
