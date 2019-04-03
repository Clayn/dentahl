package net.bplaced.clayn.d4j.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import kong.unirest.Unirest;
import net.bplaced.clayn.d4j.api.TeamEndpoint;
import net.bplaced.clayn.d4j.config.DentahlConfiguration;
import net.bplaced.clayn.d4j.config.Keys;
import net.bplaced.clayn.d4j.data.DomainData;
import net.bplaced.clayn.d4j.domain.Element;
import net.bplaced.clayn.d4j.domain.Ninja;
import net.bplaced.clayn.d4j.domain.Team;
import net.bplaced.clayn.d4j.fx.custom.FXTeam;
import net.bplaced.clayn.d4j.fx.custom.NinjaView;
import net.bplaced.clayn.d4j.fx.custom.TeamView;

public class MainWindowController implements Initializable
{

    @FXML
    private FlowPane ninjaPane;
    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane scroll;
    @FXML
    private ChoiceBox<Element> elementFilter;
    @FXML
    private VBox center;
    @FXML
    private ChoiceBox<FXTeam> teamList;
    @FXML
    private MenuItem clearMenu;
    private final TeamView team = new TeamView();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        clearMenu.disableProperty().bind(
                teamList.getSelectionModel().selectedItemProperty().isNotNull());
        teamList.setItems(DomainData.getInstance().getTeams());
        teamList.setConverter(new StringConverter<FXTeam>()
        {
            @Override
            public String toString(FXTeam object)
            {
                return object == null ? "" : object.getName();
            }

            @Override
            public FXTeam fromString(String string)
            {
                if ((string == null || string.isEmpty()))
                {
                    return null;
                }
                for (FXTeam team : teamList.getItems())
                {
                    if (string.startsWith(team.getName()))
                    {
                        return team;
                    }
                }
                return null;
            }
        });

        team.teamProperty().bind(
                teamList.getSelectionModel().selectedItemProperty());
        center.getChildren().add(0, team);
        scroll.getChildrenUnmodifiable().addListener(
                new ListChangeListener<Node>()
        {
            @Override
            public void onChanged(
                    ListChangeListener.Change<? extends Node> c)
            {
                for (Node n : c.getList())
                {
                    if (!n.getStyleClass().contains("background"))
                    {
                        n.getStyleClass().add("background");
                    }
                }
            }
        });
        elementFilter.setConverter(new StringConverter<Element>()
        {
            @Override
            public String toString(Element object)
            {
                return object == null ? "Alle" : object.translate();
            }

            @Override
            public Element fromString(String string)
            {
                for (Element el : Element.values())
                {
                    if (el.translate().equals(string))
                    {
                        return el;
                    }
                }
                return null;
            }
        });
        elementFilter.getItems().add(null);
        elementFilter.getItems().addAll(Element.values());
        elementFilter.valueProperty().addListener(new ChangeListener<Element>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Element> observable,
                    Element oldValue, Element newValue)
            {
                List<Ninja> tmp = new ArrayList<>(
                        DomainData.getInstance().getNinjas());
                DomainData.getInstance().getNinjas().clear();
                DomainData.getInstance().getNinjas().addAll(tmp);
            }
        });
        DomainData.getInstance().getNinjas().addListener(
                new ListChangeListener<Ninja>()
        {
            @Override
            public void onChanged(
                    ListChangeListener.Change<? extends Ninja> c)
            {
                ninjaPane.getChildren().clear();
                List<Node> nodes = new ArrayList<>(c.getList().size());
                for (Ninja n : DomainData.getInstance().getSortedNinjas())
                {
                    NinjaView view = new NinjaView();

                    if (elementFilter.getValue() != null)
                    {
                        if (n.getElementType() != elementFilter.getValue())
                        {
                            continue;
                        }
                    }
                    view.setNinja(n);
                    view.setPrefHeight(50);
                    view.setPrefWidth(100);
                    nodes.add(view);
                }
                ninjaPane.getChildren().addAll(nodes);
            }
        });
        try
        {
            List<Team> teams = new TeamEndpoint(
                    DentahlConfiguration.getConfiguration().get(
                            Keys.REST_BASE)).getTeams(
                    DomainData.getInstance().getNinjas());
            DomainData.getInstance().getTeams().addAll(teams.stream().map(
                    FXTeam::fromDomainTeam).collect(Collectors.toList()));
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
    }

    @FXML
    private void onClose()
    {
        root.getScene().getWindow().hide();
        Unirest.shutDown();
    }

    @FXML
    private void onClear()
    {
        team.clear();
    }

    @FXML
    private void onNewTeam()
    {
        if (teamList.getSelectionModel().getSelectedItem() == null)
        {
            onClear();
            return;
        }
        teamList.getSelectionModel().clearSelection();
    }
}
