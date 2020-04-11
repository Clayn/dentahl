package de.clayntech.dentahl4j.fx;

import de.clayntech.config4j.Config4J;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.tooling.Reflections;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class I18n
{
    private static final Logger LOG= LoggerFactory.getLogger(I18n.class);
    public static final Set<Locale> SUPPORTED_LOCALE = new HashSet<>(
            Arrays.asList(Locale.GERMAN, Locale.ENGLISH));
    private static final I18n INSTANCE = new I18n();
    private final ObjectProperty<Locale> locale = new SimpleObjectProperty<>();
    private final ReadOnlyObjectWrapper<ResourceBundle> bundle = new ReadOnlyObjectWrapper<>();

    public ResourceBundle getBundle()
    {
        return bundle.get();
    }

    public ReadOnlyObjectProperty bundleProperty()
    {
        return bundle.getReadOnlyProperty();
    }

    public Locale getLocale()
    {
        return locale.get();
    }

    public void setLocale(Locale value)
    {
        locale.set(value);
    }

    public ObjectProperty localeProperty()
    {
        return locale;
    }

    private I18n()
    {
        locale.addListener(new ChangeListener<Locale>()
        {
            @Override
            public void changed(
                    ObservableValue<? extends Locale> observable,
                    Locale oldValue, Locale newValue)
            {
                if (newValue != null)
                {
                    try
                    {
                        bundle.set(ResourceBundle.getBundle("i18n.language",
                                newValue));
                        if(oldValue==null||!newValue.equals(Config4J.getConfiguration().get(Keys.LANGUAGE))) {
                            Config4J.getConfiguration().set(Keys.LANGUAGE,newValue);
                            Config4J.saveConfiguration();
                        }
                    } catch (Exception e)
                    {
                        bundle.set(ResourceBundle.getBundle("i18n.language",
                                Locale.ROOT));
                    }
                }
            }
        });
            bundle.set(ResourceBundle.getBundle("i18n.language",
                    Locale.ROOT));
            System.out.println("Bundle set to: "+bundle.get());
        locale.set(Config4J.getConfiguration().get(Keys.LANGUAGE,Locale.ROOT));
        System.out.println("Locale set to: "+locale);
    }

    public static I18n getInstance()
    {
        return INSTANCE;
    }

    /**
     * Returns a binding that will fetch the translation for the given key if
     * the locale changed.
     *
     * @param key the key for the binding
     * @return a binding that will get the current text for the key when the
     * locale is changed
     */
    public Binding<String> getStringBinding(String key)
    {
        return Bindings.createStringBinding(() -> getBundle().getString(key),
                bundle);
    }

    public void bindTextProperty(Object object,String key) {
        Method propGetter= Reflections.findMethod(object.getClass(),"textProperty", StringProperty.class);
        if(propGetter!=null) {
            try {
                StringProperty prop= (StringProperty) propGetter.invoke(object);
                prop.bind(getStringBinding(key));
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Failed to fetch the string property",e);
            }
        }
        else {
            LOG.debug("No textProperty found for: {}",object);
        }
    }

}
