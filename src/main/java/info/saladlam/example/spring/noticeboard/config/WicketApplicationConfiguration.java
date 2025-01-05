package info.saladlam.example.spring.noticeboard.config;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import info.saladlam.example.spring.noticeboard.wicket.page.LoginPage;
import info.saladlam.example.spring.noticeboard.wicket.page.ManagePage;
import info.saladlam.example.spring.noticeboard.wicket.system.ApplicationLocalDateTimeConverter;
import info.saladlam.example.spring.noticeboard.wicket.system.ChangeLanguageRequestMapper;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.SystemMapper;
import org.apache.wicket.protocol.http.WebApplication;

import java.time.LocalDateTime;

@ApplicationInitExtension
public class WicketApplicationConfiguration implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        webApplication.mountPage("/login", LoginPage.class);
        webApplication.mountPage("/manage", ManagePage.class);

        ((SystemMapper) webApplication.getRootRequestMapper()).add(new ChangeLanguageRequestMapper());
        ((ConverterLocator) webApplication.getConverterLocator()).set(LocalDateTime.class, ApplicationLocalDateTimeConverter.INSTANCE);
        webApplication.getCspSettings().blocking().disabled();
    }

}
