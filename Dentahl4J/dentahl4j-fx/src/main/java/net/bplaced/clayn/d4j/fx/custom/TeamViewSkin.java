package net.bplaced.clayn.d4j.fx.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import net.bplaced.clayn.d4j.api.TeamEndpoint;
import net.bplaced.clayn.d4j.config.DentahlConfiguration;
import net.bplaced.clayn.d4j.config.Keys;
import net.bplaced.clayn.d4j.data.DomainData;
import net.bplaced.clayn.d4j.domain.ErrorMessage;
import net.bplaced.clayn.d4j.domain.Ninja;
import net.bplaced.clayn.d4j.domain.Team;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class TeamViewSkin extends SkinBase<TeamView>
{

    private final List<NinjaView> views = new ArrayList<>();
    private final IntegerProperty ninjaCount = new SimpleIntegerProperty(0);
    private final TextField nameField = new TextField();
    private final TextArea descriptionArea = new TextArea();
    private final TextField tokenField = new TextField();
    private final TeamView teamView;

    TeamViewSkin(TeamView control)
    {
        super(control);
        teamView = control;
        HBox box = new HBox(10);
        control.setOnClear(new Runnable()
        {
            @Override
            public void run()
            {
                views.forEach((v) -> v.setNinja(null));
            }
        });
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.getStyleClass().add("background");
        for (int i = 0; i < 3; ++i)
        {
            ColumnConstraints col = new ColumnConstraints();
            RowConstraints row = new RowConstraints();
            grid.getColumnConstraints().add(col);
            grid.getRowConstraints().add(row);
        }
        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 3; ++x)
            {
                NinjaView view = new NinjaView();
                view.setPrefSize(100, 50);
                view.ninjaProperty().addListener(new ChangeListener<Ninja>()
                {
                    @Override
                    public void changed(
                            ObservableValue<? extends Ninja> observable,
                            Ninja oldValue, Ninja newValue)
                    {
                        if (oldValue != null && newValue == null)
                        {
                            ninjaCount.set(ninjaCount.get() - 1);
                        } else if (oldValue == null && newValue != null)
                        {
                            ninjaCount.set(ninjaCount.get() + 1);
                        }
                    }

                });
                view.setNinja(
                        control.getTeam() == null ? null : control.getTeam().getPositions().get(
                        views.size()));

                view.setXPosition(x);
                view.setYPosition(y);
                view.dragableAccessProperty().bind(
                        control.teamProperty().isNull());
                view.dragableDetailProperty().bind(ninjaCount.lessThan(
                        4));
                GridPane.setColumnIndex(view, x);
                GridPane.setRowIndex(view, y);
                grid.getChildren().add(view);
                views.add(view);
            }
        }
        control.teamProperty().addListener(new ChangeListener<FXTeam>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends FXTeam> observable,
                    FXTeam oldValue, FXTeam newValue)
            {
                if (newValue == null)
                {
                    views.stream().forEach((v) -> v.setNinja(null));
                } else
                {
                    for (Map.Entry<Integer, Ninja> entry : newValue.getPositions().entrySet())
                    {
                        int index = entry.getKey();
                        if (index >= 0 && index < views.size())
                        {
                            views.get(index).setNinja(entry.getValue());
                        }
                    }
                }
            }
        });
        VBox details = new VBox(10);
        VBox nameBox = new VBox(5);
        Label l = new Label("Teamname");
        l.getStyleClass().add("item-title");
        addListener(control, nameField, Team::getName);
        nameBox.getChildren().addAll(l, nameField);
        VBox descriptionBox = new VBox(5);
        l = new Label("Teambeschreibung");
        l.getStyleClass().add("item-title");
        addListener(control, descriptionArea, Team::getDescription);
        descriptionBox.getChildren().addAll(l, descriptionArea);
        VBox tokenBox = new VBox(5);
        l = new Label("Token");
        l.getStyleClass().add("item-title");
        tokenBox.getChildren().addAll(l, tokenField);
        Button upload = new Button("Speichern");
        upload.setOnAction(this::uploadTeam);
        details.getChildren().addAll(nameBox, descriptionBox, tokenBox, upload);
        nameField.editableProperty().bind(details.disableProperty().not());
        box.disableProperty().bind(control.teamProperty().isNotNull());
        box.getChildren().addAll(grid, details);
        getChildren().add(box);
    }

    private void uploadTeam(ActionEvent evt)
    {
        TeamEndpoint end = new TeamEndpoint(
                DentahlConfiguration.getConfiguration().get(Keys.REST_BASE));
        FXTeam t = new FXTeam();
        t.setName(nameField.getText());
        t.setDescription(descriptionArea.getText());
        for (int i = 0; i < views.size(); ++i)
        {
            t.getPositions().put(i, views.get(i).getNinja());
        }
        try
        {
            ErrorMessage mes = end.uploadTeam(t, tokenField.getText());
            try
            {
                Long.parseLong(mes.getMessage());
                DomainData.getInstance().getTeams().add(t);
            } catch (Exception ex)
            {
                System.out.println("Error uploading");
                ex.printStackTrace();
            }
        } catch (IOException ex)
        {
            Logger.getLogger(TeamViewSkin.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private void addListener(TeamView view, TextInputControl control,
            Function<Team, String> getter)
    {
        view.teamProperty().addListener(new ChangeListener<FXTeam>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends FXTeam> observable,
                    FXTeam oldValue, FXTeam newValue)
            {
                control.setText(newValue == null ? "" : getter.apply(newValue));
            }
        });
    }
}
