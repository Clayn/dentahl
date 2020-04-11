package de.clayntech.dentahl4j.fx.util;

import javafx.scene.image.Image;

import java.io.InputStream;

public enum DentahlImage {
    ICON("icon.png"),EMPTY("empty.png");

    public static final Image ICON_IMAGE=ICON.createDefaultInstance();

    private static class Default extends Image{
        public Default(InputStream inputStream) {
            super(inputStream);
        }
    }

    private final String resourcePath;

    DentahlImage(String imgName) {
        this.resourcePath = "/images/"+imgName;
    }

    private Default createDefaultInstance() {
        return new Default(getClass().getResourceAsStream(resourcePath));
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public Image load() {
       return new Image(getClass().getResourceAsStream(
                resourcePath));
    }
}
