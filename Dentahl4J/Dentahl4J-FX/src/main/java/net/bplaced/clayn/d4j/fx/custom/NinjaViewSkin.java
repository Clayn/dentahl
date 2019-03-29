/*
 * The MIT License
 *
 * Copyright 2019 Your Organisation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.bplaced.clayn.d4j.fx.custom;

import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import net.bplaced.clayn.d4j.data.DomainData;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class NinjaViewSkin extends SkinBase<NinjaView> {

    NinjaViewSkin(NinjaView control) {
        super(control);
        ImageView view = new ImageView();
        view.imageProperty().bind(Bindings.createObjectBinding(
                new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                return DomainData.getInstance().getNinjaImages().getOrDefault(
                        control.getNinja(), null);
            }

        },
                control.ninjaProperty()));
        view.fitHeightProperty().bind(control.prefHeightProperty());
        view.fitWidthProperty().bind(control.prefWidthProperty());

        StackPane pane = new StackPane(view);
        pane.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Drag detected");
                if (!control.isDragable()) {
                    System.out.println("No drag allowed");
                    return;
                }
                System.out.println("Draaaging");
                final Dragboard db = pane.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                final ClipboardContent cc = new ClipboardContent();
                cc.putString("Foo");
                db.setDragView(view.getImage());
                db.setContent(cc);
                event.consume();
            }
        });
        pane.getStyleClass().add("ninjaview");
        pane.prefWidthProperty().bind(control.prefWidthProperty());
        pane.prefHeightProperty().bind(control.prefHeightProperty());
        getChildren().add(pane);
    }

}
