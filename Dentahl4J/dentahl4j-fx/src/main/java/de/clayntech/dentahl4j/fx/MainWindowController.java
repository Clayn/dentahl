package de.clayntech.dentahl4j.fx;

import de.clayntech.config4j.Config4J;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.data.DomainData;
import de.clayntech.dentahl4j.domain.Element;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.domain.Team;
import de.clayntech.dentahl4j.fx.api.FXTeamService;
import de.clayntech.dentahl4j.fx.custom.FXTeam;
import de.clayntech.dentahl4j.fx.custom.NinjaView;
import de.clayntech.dentahl4j.fx.custom.TeamView;
import de.clayntech.dentahl4j.fx.util.UIUtils;
import de.clayntech.dentahl4j.tooling.ApplicationTask;
import de.clayntech.dentahl4j.tooling.TaskManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import kong.unirest.Unirest;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainWindowController implements Initializable {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MainWindowController.class);
    private final TeamView team = new TeamView();
    @FXML
    public Button newButton;
    @FXML
    public Button refreshButton;
    @FXML
    private FlowPane ninjaPane;
    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane scroll;
    @FXML
    private ChoiceBox<Element> elementFilter;
    @FXML
    private TextField nameFilter;
    @FXML
    private VBox center;
    @FXML
    private ChoiceBox<FXTeam> teamList;
    @FXML
    private MenuItem clearMenu;
    @FXML
    private Menu languageMenu;

    @FXML
    private MenuBar menuBar;

    private void translateMenu(MenuItem menu) {
        if(!menu.getText().isBlank()) {
            menu.textProperty().bind(I18n.getInstance().getStringBinding(menu.getText()));
        }
        if(menu instanceof Menu) {
            Menu m= (Menu) menu;
            for(MenuItem mi:m.getItems()) {
                translateMenu(mi);
            }
        }
    }

    private void translateMenuBar() {
        for(Menu m:menuBar.getMenus()) {
            translateMenu(m);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        translateMenuBar();
        I18n.getInstance().bindTextProperty(newButton,newButton.getText());
        I18n.getInstance().bindTextProperty(refreshButton,refreshButton.getText());
        clearMenu.disableProperty().bind(
                teamList.getSelectionModel().selectedItemProperty().isNotNull());
        teamList.setItems(DomainData.getInstance().getTeams());
        ToggleGroup langGroup = new ToggleGroup();
        for (Locale l : I18n.SUPPORTED_LOCALE) {
            RadioMenuItem item = new RadioMenuItem();
            item.textProperty().bind(I18n.getInstance().getStringBinding(
                    "language." + l.getLanguage()));
            langGroup.getToggles().add(item);
            item.setToggleGroup(langGroup);
            if (l.getLanguage().equals(
                    I18n.getInstance().getLocale().getLanguage())) {
                langGroup.selectToggle(item);
                item.setSelected(true);
            }
            item.setUserData(l);
            languageMenu.getItems().add(item);
        }
        langGroup.selectedToggleProperty().addListener(
                new ChangeListener<Toggle>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Toggle> observable,
                            Toggle oldValue, Toggle newValue) {
                        if (newValue != null) {
                            Object ud = newValue.getUserData();
                            LOG.debug("Selected user data: {}", ud);
                            if (ud instanceof Locale) {
                                I18n.getInstance().setLocale((Locale) ud);
                            }
                        }
                    }
                });
        teamList.setConverter(new StringConverter<FXTeam>() {
            @Override
            public String toString(FXTeam object) {
                return object == null ? "" : object.getName();
            }

            @Override
            public FXTeam fromString(String string) {
                if ((string == null || string.isEmpty())) {
                    return null;
                }
                for (FXTeam team : teamList.getItems()) {
                    if (string.startsWith(team.getName())) {
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
                new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(
                            ListChangeListener.Change<? extends Node> c) {
                        for (Node n : c.getList()) {
                            if (!n.getStyleClass().contains("background")) {
                                n.getStyleClass().add("background");
                            }
                        }
                    }
                });
        elementFilter.setConverter(new StringConverter<Element>() {
            @Override
            public String toString(Element object) {
                return object == null ? I18n.getInstance().getBundle().getString(
                        "element.all") : object.translate(
                        I18n.getInstance().getBundle());
            }

            @Override
            public Element fromString(String string) {
                for (Element el : Element.values()) {
                    if (el.translate(I18n.getInstance().getBundle()).equals(
                            string)) {
                        return el;
                    }
                }
                return null;
            }
        });
        I18n.getInstance().bundleProperty().addListener(
                new ChangeListener<ResourceBundle>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends ResourceBundle> observable,
                            ResourceBundle oldValue, ResourceBundle newValue) {
                        if (newValue != null) {
                            Element selected = elementFilter.getValue();
                            elementFilter.getItems().clear();
                            elementFilter.getItems().add(null);
                            elementFilter.getItems().addAll(Element.values());
                            if (selected != null) {
                                Platform.runLater(
                                        () -> elementFilter.getSelectionModel().select(
                                                selected));
                            }
                        }
                    }

                });
        elementFilter.getItems().add(null);
        elementFilter.getItems().addAll(Element.values());
        elementFilter.valueProperty().addListener(new ChangeListener<Element>() {
            @Override
            public void changed(
                    ObservableValue<? extends Element> observable,
                    Element oldValue, Element newValue) {
                List<Ninja> tmp = new ArrayList<>(
                        DomainData.getInstance().getNinjas());
                DomainData.getInstance().getNinjas().clear();
                DomainData.getInstance().getNinjas().addAll(tmp);
            }
        });
        DomainData.getInstance().nameFilterProperty().bind(nameFilter.textProperty());
        DomainData.getInstance().getFilteredNinjas().addListener(
                new ListChangeListener<Ninja>() {
                    @Override
                    public void onChanged(
                            ListChangeListener.Change<? extends Ninja> c) {
                        ninjaPane.getChildren().clear();
                        List<Node> nodes = new ArrayList<>(c.getList().size());
                        List<Ninja> tmp = new ArrayList<>(c.getList());
                        tmp.sort(
                                Comparator.comparingInt(Ninja::getMain).reversed().thenComparingInt(
                                        Ninja::getId));
                        for (Ninja n : tmp) {
                            NinjaView view = new NinjaView();

                            if (elementFilter.getValue() != null) {
                                if (n.getElementType() != elementFilter.getValue()) {
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
        try {
            TaskManager.getTaskManager().registerTask(Keys.TASK_LOAD_TEAMS, new ApplicationTask<List<Team>>() {
                @Override
                public List<Team> execute() throws Exception {
                    return new FXTeamService(Config4J.getConfiguration().get(Keys.REST_BASE))
                            .setCallback(new Consumer<List<Team>>() {
                                @Override
                                public void accept(List<Team> teams) {
                                    DomainData.getInstance().getTeams().addAll(teams.stream().map(
                                            FXTeam::fromDomainTeam).collect(Collectors.toList()));
                                }
                            }).getTeams();
                }
            });
            TaskManager.getTaskManager().callTask(Keys.TASK_LOAD_TEAMS);
        } catch (Exception ex) {
            LOG.error("Failed to load the teams", ex);
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void onClose() {
        root.getScene().getWindow().hide();
        Unirest.shutDown();
    }

    @FXML
    private void onClear() {
        team.clear();
    }

    @FXML
    private void onNewTeam() {
        if (teamList.getSelectionModel().getSelectedItem() == null) {
            onClear();
            return;
        }
        teamList.getSelectionModel().clearSelection();
    }

    public void onRefresh(ActionEvent actionEvent) {
        TaskManager.getTaskManager().callTask(Keys.TASK_LOAD_TEAMS);
    }

    public void showAbout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/dialog/UpdateDialog.fxml"));
        loader.setResources(I18n.getInstance().getBundle());
        Parent p=loader.load();
        p.getStyleClass().add("background");
        Scene sc=new Scene(p);
        JMetro metro = new JMetro(sc, Style.DARK);
        Stage st=new Stage();
        UIUtils.setStageIcon(st);
        st.setScene(sc);
        st.initModality(Modality.APPLICATION_MODAL);
        st.initOwner(root.getScene().getWindow());
        st.show();
    }
}
