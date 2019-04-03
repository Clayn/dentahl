package net.bplaced.clayn.d4j.fx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class I18n
{

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
                        System.out.println(
                                "Loading resourcebundle for locale: " + newValue);
                        bundle.set(ResourceBundle.getBundle("i18n.language",
                                newValue));
                    } catch (Exception e)
                    {
                        System.out.println(
                                "No resourcebundle found for locale: " + newValue);
                        bundle.set(ResourceBundle.getBundle("i18n.language",
                                Locale.ROOT));
                    }
                }
            }
        });
        locale.setValue(Locale.getDefault());
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

}
