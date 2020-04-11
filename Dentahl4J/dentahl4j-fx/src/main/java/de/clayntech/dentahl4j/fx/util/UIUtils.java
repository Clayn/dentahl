package de.clayntech.dentahl4j.fx.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UIUtils {

    public static void setStageIcon(Stage st) {
        st.getIcons().add(DentahlImage.ICON_IMAGE);
    }
}
