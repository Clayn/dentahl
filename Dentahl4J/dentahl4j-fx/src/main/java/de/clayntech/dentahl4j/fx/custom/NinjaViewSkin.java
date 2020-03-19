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
package de.clayntech.dentahl4j.fx.custom;

import de.clayntech.dentahl4j.data.DomainData;
import de.clayntech.dentahl4j.domain.Ninja;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;

import java.util.concurrent.Callable;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class NinjaViewSkin extends SkinBase<NinjaView>
{

    private boolean dragged = false;
    private static final DataFormat NINJA_DATA_FORMAT = new DataFormat(
            Ninja.class.getTypeName());

    NinjaViewSkin(NinjaView control)
    {
        super(control);
        ImageView view = new ImageView();
        view.imageProperty().bind(Bindings.createObjectBinding(
                new Callable<Image>()
        {
            @Override
            public Image call() throws Exception
            {
                return DomainData.getInstance().getNinjaImages().getOrDefault(
                        control.getNinja(), null);
            }

        },
                control.ninjaProperty()));
        view.fitHeightProperty().bind(control.prefHeightProperty());
        view.fitWidthProperty().bind(control.prefWidthProperty());

        StackPane pane = new StackPane(view);
        pane.setOnDragDetected(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (control.getNinja() == null || !control.isDragable() || (!control.isDragable() && !control.isDragableDetail()))
                {
                    return;
                }
                final Dragboard db = pane.startDragAndDrop(
                        TransferMode.COPY_OR_MOVE);
                final ClipboardContent cc = new ClipboardContent();
                cc.put(NINJA_DATA_FORMAT, control.getNinja());
                db.setDragView(view.getImage());
                db.setContent(cc);
                event.consume();
            }
        });
        pane.setOnDragOver(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                if (event.getGestureSource() != pane
                        && event.getDragboard().hasContent(NINJA_DATA_FORMAT) && control.isDragable() && control.isDragableDetail())
                {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        pane.setOnDragEntered(new EventHandler<DragEvent>()
        {
            public void handle(DragEvent event)
            {
                if (control.isTeamPosition() && event.getGestureSource() != pane
                        && event.getDragboard().hasContent(NINJA_DATA_FORMAT) && control.isDragable() && control.isDragableDetail())
                {
                    if (!pane.getStyleClass().contains("team-drag-over"))
                    {
                        pane.getStyleClass().add("team-drag-over");
                    }
                }

                event.consume();
            }
        });
        pane.setOnDragExited(new EventHandler<DragEvent>()
        {
            public void handle(DragEvent event)
            {
                pane.getStyleClass().remove("team-drag-over");
                event.consume();
            }
        });
        pane.setOnDragDropped(new EventHandler<DragEvent>()
        {
            public void handle(DragEvent event)
            {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasContent(NINJA_DATA_FORMAT))
                {
                    if (control.isTeamPosition())
                    {
                        control.setNinja(
                                (Ninja) db.getContent(NINJA_DATA_FORMAT));
                    }
                    success = true;
                }
                event.setDropCompleted(success);

                event.consume();
            }
        });
        pane.setOnDragDone(new EventHandler<DragEvent>()
        {
            public void handle(DragEvent event)
            {
                if (event.getTransferMode() == TransferMode.MOVE || event.getTransferMode() == TransferMode.COPY)
                {
                    dragged = true;
                    if (control.isTeamPosition())
                    {
                        DomainData.getInstance().getNinjas().add(
                                control.getNinja());
                    }
                    control.setNinja(null);

                    dragged = false;
                }
                event.consume();
            }
        });
        ChangeListener<Ninja> listener = new ChangeListener<Ninja>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Ninja> observable, Ninja oldValue,
                    Ninja newValue)
            {
                if (oldValue != null && !dragged)
                {
                    DomainData.getInstance().getNinjas().add(oldValue);
                }
                if (newValue != null)
                {
                    DomainData.getInstance().getNinjas().remove(newValue);
                }
            }
        };
        control.xPositionProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue)
            {
                control.ninjaProperty().removeListener(listener);
                if (control.isTeamPosition())
                {
                    control.ninjaProperty().addListener(listener);
                }
            }
        });
        control.yPositionProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue)
            {
                control.ninjaProperty().removeListener(listener);
                if (control.isTeamPosition())
                {
                    control.ninjaProperty().addListener(listener);
                }
            }
        });
        if (control.isTeamPosition())
        {
            control.ninjaProperty().addListener(listener);
        }
        pane.getStyleClass().add("ninjaview");
        pane.prefWidthProperty().bind(control.prefWidthProperty());
        pane.prefHeightProperty().bind(control.prefHeightProperty());
        Tooltip tip = new Tooltip();
        tip.textProperty().bind(Bindings.createStringBinding(
                () -> control.getNinja() == null ? "Unkown" : control.getNinja().getName(),
                control.ninjaProperty()));
        Tooltip.install(pane, tip);
        getChildren().add(pane);
    }

}
