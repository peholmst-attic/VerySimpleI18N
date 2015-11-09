package org.vaadin.webinar.i18n;

import java.util.Locale;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;

@Theme(ValoTheme.THEME_NAME)
public class VerySimpleI18nUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        MainView mainView = new MainView();
        setContent(mainView);
        setLocale(vaadinRequest.getLocale());
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        updateMessageStrings(getContent());
    }

    private void updateMessageStrings(Component component) {
        if (component instanceof Translatable) {
            ((Translatable) component).updateMessageStrings();
        }
        if (component instanceof HasComponents) {
            ((HasComponents) component).iterator().forEachRemaining(this::updateMessageStrings);
        }
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = VerySimpleI18nUI.class, productionMode = false)
    public static class Servlet extends VaadinServlet {
    }
}
