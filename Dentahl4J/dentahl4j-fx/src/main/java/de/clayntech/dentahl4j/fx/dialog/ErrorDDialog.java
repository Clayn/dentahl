package de.clayntech.dentahl4j.fx.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class ErrorDDialog extends DDialog {

    private final Throwable exception;

    public ErrorDDialog(Throwable exception) {
        this.exception = exception;
    }

    @Override
    protected Alert prepareAlert(DDialogConfiguration config) {
        if(config==null) {
            return null;
        }
        Alert al=super.prepareAlert(config);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);
        al.getDialogPane().setExpandableContent(expContent);
        if(config.isShowMessage()) {
            al.setContentText(String.format(al.getContentText(), exception.getClass().getSimpleName()));
        }
        return al;
    }
}
