package net.bplaced.clayn.d4j.fx;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import net.bplaced.clayn.d4j.data.DomainData;
import net.bplaced.clayn.d4j.domain.Ninja;
import net.bplaced.clayn.d4j.fx.custom.NinjaView;

public class MainWindowController implements Initializable {
    
    @FXML
    private FlowPane ninjaPane;
    private final Map<Ninja,Node> ninjaNodes=new HashMap<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DomainData.getInstance().getNinjaImages().addListener(new MapChangeListener<Ninja, Image>() {
            @Override
            public void onChanged(
                    MapChangeListener.Change<? extends Ninja, ? extends Image> change) {
                    if(change.wasRemoved()) {
                        if(ninjaNodes.containsKey(change.getKey())) {
                            ninjaPane.getChildren().remove(ninjaNodes.remove(
                                    change.getKey()));
                        }
                    }
                    if(change.wasAdded()) {
                        if(!ninjaNodes.containsKey(change.getKey())) {
                            NinjaView view=new NinjaView();
                            view.setNinja(change.getKey());
                            view.setPrefHeight(50);
                            view.setPrefWidth(100);
                            view.setDragable(true);
                            ninjaNodes.put(change.getKey(), view);
                            ninjaPane.getChildren().add(ninjaNodes.get(change.getKey()));
                        }
                    }
            }
        });
    }    
}
