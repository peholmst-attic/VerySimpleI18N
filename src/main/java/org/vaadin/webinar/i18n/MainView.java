package org.vaadin.webinar.i18n;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.ui.*;

public class MainView extends VerticalLayout implements Translatable {

    private LanguageSelector languageSelector;
    private Grid grid;
    private IndexedContainer container;
    private Label containerCount;
    private Button generateRows;
    private Random rnd;
    private TextField bigDecimalTextField;
    private DateField dateField;
    private ObjectProperty<BigDecimal> bigDecimalObjectProperty = new ObjectProperty<>(new BigDecimal("1230500.25"));

    public MainView() {
        rnd = new Random();
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSpacing(true);
        addComponent(topBar);

        languageSelector = new LanguageSelector();
        topBar.addComponent(languageSelector);

        bigDecimalTextField = new TextField();
        bigDecimalTextField.setConverter(new StringToBigDecimalConverter());
        bigDecimalTextField.setPropertyDataSource(bigDecimalObjectProperty);
        bigDecimalTextField.setImmediate(true);
        bigDecimalTextField.setNullSettingAllowed(false);
        topBar.addComponent(bigDecimalTextField);

        dateField = new DateField();
        dateField.setValue(new Date());
        topBar.addComponent(dateField);

        container = new IndexedContainer();
        container.addContainerProperty("subject", String.class, "");
        container.addContainerProperty("sender", String.class, "");
        container.addContainerProperty("date", Instant.class, "");
        grid = new Grid(container);
        grid.getColumn("date").setConverter(new InstantConverter());
        grid.setSizeFull();
        addComponent(grid);
        setExpandRatio(grid, 1.0f);

        containerCount = new Label();
        addComponent(containerCount);

        generateRows = new Button();
        generateRows.addClickListener(this::generateRows);
        addComponent(generateRows);
    }

    private int lastNumOfRows = 1;

    @SuppressWarnings("unchecked")
    private void generateRows(Button.ClickEvent event) {
        container.removeAllItems();
        if (lastNumOfRows == 1) {
            lastNumOfRows = rnd.nextInt(50);
        } else {
            lastNumOfRows = 1;
        }
        for (int i = 0; i < lastNumOfRows; ++i) {
            Item item = container.addItem(i);
            item.getItemProperty("subject").setValue(PIGLATIN[rnd.nextInt(PIGLATIN.length)]);
            item.getItemProperty("sender").setValue(PIGLATIN[rnd.nextInt(PIGLATIN.length)]);
            item.getItemProperty("date").setValue(Instant.now().minusSeconds(rnd.nextInt(10000000)));
        }
        updateContainerCount();
    }

    @Override
    public void updateMessageStrings() {
        final Messages messages = Messages.getInstance();
        languageSelector.setCaption(messages.getMessage("languageSelector.caption"));
        bigDecimalTextField.setCaption(messages.getMessage("bigDecimalTextField.caption"));
        bigDecimalTextField.setConversionError(messages.getMessage("bigDecimalTextField.conversionError"));
        dateField.setCaption(messages.getMessage("dateField.caption"));
        grid.getColumn("subject").setHeaderCaption(messages.getMessage("grid.subject.caption"));
        grid.getColumn("sender").setHeaderCaption(messages.getMessage("grid.sender.caption"));
        grid.getColumn("date").setHeaderCaption(messages.getMessage("grid.date.caption"));
        grid.setCellStyleGenerator(null); // Ugly hack to force the Grid to refresh itself
        updateContainerCount();
        generateRows.setCaption(messages.getMessage("generateRows.caption"));

        bigDecimalTextField.setLocale(getLocale());
        dateField.setLocale(getLocale());
    }

    private void updateContainerCount() {
        final Messages messages = Messages.getInstance();
        if (container.size() == 1) {
            containerCount.setValue(messages.getMessage("messageCount.caption.single", container.size()));
        } else {
            containerCount.setValue(messages.getMessage("messageCount.caption.multiple", container.size()));
        }
    }

    private static String[] PIGLATIN = { "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        "Morbi et dolor pellentesque, congue nulla id, euismod nulla.", "Donec scelerisque sed mauris quis lobortis.",
        "Integer sollicitudin felis at nibh faucibus bibendum.",
        "Vivamus diam urna, accumsan et turpis sed, porttitor ullamcorper tellus.", "Vivamus id aliquet turpis.",
        "Nunc quis justo vestibulum, tempus elit quis, facilisis nisi.",
        "Donec quis libero at nunc consectetur blandit." };

    private static class InstantConverter implements Converter<String, Instant> {

        @Override
        public Instant convertToModel(String value, Class<? extends Instant> targetType, Locale locale)
            throws ConversionException {
            throw new ConversionException("This is a one-way converter");
        }

        @Override
        public String convertToPresentation(Instant value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale).format(ZonedDateTime.ofInstant(
                value, ZoneId.systemDefault()));
        }

        @Override
        public Class<Instant> getModelType() {
            return Instant.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    }
}
