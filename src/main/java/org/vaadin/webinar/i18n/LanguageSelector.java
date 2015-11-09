package org.vaadin.webinar.i18n;

import java.util.Locale;

import com.vaadin.ui.ComboBox;

public class LanguageSelector extends ComboBox implements Translatable {

    private static final Locale SWEDISH = new Locale("sv");
    private static final Locale FINNISH = new Locale("fi");

    public LanguageSelector() {
        addItem(Locale.ENGLISH);
        addItem(FINNISH);
        addItem(SWEDISH);
        setValue(Locale.ENGLISH);
        setNullSelectionAllowed(false);
        addValueChangeListener(e -> getUI().setLocale((Locale) getValue()));
    }

    @Override
    public void attach() {
        super.attach();
        setValue(getUI().getLocale());
    }

    @Override
    public void updateMessageStrings() {
        Messages messages = Messages.getInstance();
        setItemCaption(Locale.ENGLISH, messages.getMessage("languageSelector.en"));
        setItemCaption(SWEDISH, messages.getMessage("languageSelector.sv"));
        setItemCaption(FINNISH, messages.getMessage("languageSelector.fi"));
    }
}
