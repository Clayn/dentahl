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
package de.clayntech.dentahl4j.fx.pre;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import de.clayntech.config4j.Config4J;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import de.clayntech.dentahl4j.api.NinjaServiceEndpoint;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.data.DomainData;
import de.clayntech.dentahl4j.domain.Ninja;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class D4JFXPreloader implements Initializable
{

    private final ReadOnlyBooleanWrapper finished = new ReadOnlyBooleanWrapper(
            false);

    private abstract class ProgressTask implements Runnable
    {

        public void setProgress(double progress)
        {
            Platform.runLater(() -> progressTask.setProgress(progress));
        }

        public void setText(String text)
        {
            Platform.runLater(() -> labelTask.setText(text));
        }

    }

    private final List<ProgressTask> tasks = new ArrayList<>();
    @FXML
    private Label labelAll;
    @FXML
    private ProgressBar progressAll;
    @FXML
    private Label labelTask;
    @FXML
    private ProgressBar progressTask;

    public boolean isFinished()
    {
        return finished.get();
    }

    public ReadOnlyBooleanProperty finishedProperty()
    {
        return finished.getReadOnlyProperty();
    }

    public void doWork(Stage st, JMetro metro) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fxml/Preloader.fxml"));
        Parent root = loader.load();
        D4JFXPreloader cont = loader.getController();
        Scene scene = new Scene(root);
        metro.applyTheme(scene);
        metro.applyTheme(root);
        st.setScene(scene);
        st.show();
        cont.start(finished);
    }

    private void start(ReadOnlyBooleanWrapper prop)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                double taskCount = tasks.size();
                double finishedTasks = 0;
                String startTest = String.format("Task %d / %d",
                        (int) finishedTasks,
                        (int) taskCount);
                if (labelAll == null)
                {
                    System.out.println("Why is the label null?");
                }
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (labelAll == null)
                        {
                            System.out.println("Why is the label null2?");
                        }
                        labelAll.setText(startTest);
                    }
                });
                setProgress(finishedTasks / taskCount, 0);
                for (ProgressTask task : tasks)
                {
                    task.run();
                    finishedTasks++;
                    setProgress(finishedTasks / taskCount, 0);
                    String text = String.format("Task %d / %d",
                            (int) finishedTasks,
                            (int) taskCount);
                    Platform.runLater(() -> labelAll.setText(text));
                }
                Platform.runLater(() -> prop.set(true));
            }
        }).start();
    }

    private void setProgress(double all, double task)
    {
        Platform.runLater(() ->
        {
            progressAll.setProgress(all);
            progressTask.setProgress(task);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        progressAll.setProgress(-1);
        progressTask.setProgress(-1);
        Objects.requireNonNull(labelAll);
        Objects.requireNonNull(labelTask);
        initTasks();
    }

    private final List<Ninja> ninjas = new ArrayList<>();

    private String getTimeString(long nano)
    {
        long hour = TimeUnit.NANOSECONDS.toHours(nano);
        long min = TimeUnit.NANOSECONDS.toMinutes(nano);
        long sec = TimeUnit.NANOSECONDS.toSeconds(nano);
        if (hour > 0)
        {
            return String.format("%d h %d min %d s", hour, min, sec);
        } else if (min > 0)
        {
            return String.format("%d min %d s", min, sec);
        } else if (sec > 0)
        {
            return String.format("%d s", sec);
        }
        return "";
    }
    
    private InputStream read(URL url) throws IOException {
    HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
    httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

  return httpcon.getInputStream();
}

    private void initTasks()
    {
        tasks.add(new ProgressTask()
        {
            @Override
            public void run()
            {
                try
                {
                    setText("Lade Ninjas");
                    setProgress(-1);
                    ninjas.addAll(new NinjaServiceEndpoint(
                            Config4J.getConfiguration().get(
                                    Keys.REST_BASE)).getNinjaList());
                    File dir = new File("data", "ninjas");
                    dir.mkdirs();
                    double count = ninjas.size();
                    double done = 0;
                    setProgress(0);
                    int i = 1;
                    for (Ninja n : ninjas)
                    {
                        setText(String.format("Lade Ninja %d / %d", i++,
                                ninjas.size()));
                        File f = new File(dir, "" + n.getId() + ".ninja");
                        if (!f.exists())
                        {
                            f.createNewFile();
                            Properties prop = new Properties();
                            prop.setProperty("name", n.getName());
                            prop.setProperty("image",
                                    n.getImage() == null ? "" : n.getImage().toString());
                            prop.setProperty("id", "" + n.getId());
                            prop.setProperty("element", n.getElement() + "");
                            try (OutputStream fout = Files.newOutputStream(
                                    f.toPath()))
                            {
                                prop.store(fout, "");
                                fout.flush();
                            }

                        }
                        done++;
                        setProgress(done / count);
                    }
                    DomainData.getInstance().getNinjas().clear();
                    /*List<Ninja> mains = ninjas.stream().filter(Ninja::isMain)
                            .sorted(Comparator.comparingInt(Ninja::getId)).collect(
                            Collectors.toList());
                    List<Ninja> normal = ninjas.stream().filter(
                            (n) -> !n.isMain())
                            .sorted(Comparator.comparingInt(Ninja::getId)).collect(
                            Collectors.toList());
                    DomainData.getInstance().getNinjas().addAll(mains);
                    DomainData.getInstance().getNinjas().addAll(normal);*/
 /*ninjas.sort(
                            Comparator.comparingInt(Ninja::getMain).reversed().thenComparingInt(
                                    Ninja::getId));*/
                    DomainData.getInstance().getNinjas().addAll(ninjas);
                    System.out.println("All added");
                } catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        });
        tasks.add(new ProgressTask()
        {
            @Override
            public void run()
            {
                try
                {
                    int tries = 10;
                    setText("Lade Ninjabilder");
                    setProgress(-1);
                    double count = ninjas.size();
                    double done = 0;
                    setProgress(0);
                    int i = 1;
                    File dir = new File("data", "images");
                    dir.mkdirs();
                    int remain = ninjas.size();
                    long rest = -1;
                    long delta = -1;
                    for (Ninja n : ninjas)
                    {

                        if (rest < 0)
                        {
                            setText(String.format("Lade Bild %d / %d", i++,
                                    ninjas.size()));
                        } else
                        {
                            setText(String.format(
                                    "Lade Bild %d / %d. Eta: %s", i++,
                                    ninjas.size(), getTimeString(rest)));
                        }
                        File imgFile = new File(dir, n.getId() + ".png");
                        long time1 = System.nanoTime();
                        if (!imgFile.exists())
                        {
                            URL u = n.getImage();
                            if (u != null)
                            {

                                imgFile.createNewFile();
                                int tried = 0;
                                do
                                {
                                    try (InputStream in = read(u); OutputStream out = Files.newOutputStream(
                                            imgFile.toPath()))
                                    {
                                        byte[] buffer = new byte[256];
                                        int read = 0;
                                        while ((read = in.read(buffer)) >= 0)
                                        {
                                            out.write(buffer, 0, read);
                                        }
                                        out.flush();
                                        break;
                                    } catch (Exception e)
                                    {
                                        System.err.println(
                                                "Catched an exception, try again. " + tried + " / " + tries);
                                        tried++;
                                    }
                                } while (tried < tries);
                                if (tried >= tries && imgFile.exists())
                                {
                                    imgFile.delete();
                                }
                            }
                        }
                        if (imgFile.exists())
                        {
                            try (InputStream in = Files.newInputStream(
                                    imgFile.toPath()))
                            {
                                Image img = new Image(in);
                                DomainData.getInstance().getNinjaImages().put(
                                        n,
                                        img);
                            }
                        }
                        long time2 = System.nanoTime();
                        if (delta < 0)
                        {
                            delta = time2 - time1;
                        }
                        done++;
                        remain--;
                        rest = remain * delta;
                        setProgress(done / count);
                    }
                } catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
