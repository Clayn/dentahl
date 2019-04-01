package net.bplaced.clayn.d4j.fx.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import net.bplaced.clayn.d4j.domain.Ninja;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class TeamViewSkin extends SkinBase<TeamView>
{

    private final List<NinjaView> views = new ArrayList<>();
    private final IntegerProperty ninjaCount = new SimpleIntegerProperty(0);

    TeamViewSkin(TeamView control)
    {
        super(control);
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
        getChildren().add(grid);
    }

}
