package info.saladlam.example.spring.noticeboard.wicket.system;

import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import org.apache.wicket.Session;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class ChangeLanguageRequestHandler implements IRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeLanguageRequestHandler.class);

    @Override
    public void respond(IRequestCycle requestCycle) {
        List<Url.QueryParameter> parameters = requestCycle.getRequest().getUrl().getQueryParameters();
        String lang = "";
        for (Url.QueryParameter p : parameters) {
            if (ApplicationConstant.CHANGE_LOCALE_PARAMETER.equals(p.getName())) {
                lang = p.getValue();
                break;
            }
        }
        switch (lang) {
            case ApplicationConstant.LOCALE_EN:
            case ApplicationConstant.LOCALE_JA:
                Session.get().setLocale(new Locale(lang));

                Url redirectUrl = new Url(requestCycle.getRequest().getUrl());
                redirectUrl.removeQueryParameters(ApplicationConstant.CHANGE_LOCALE_PARAMETER);
                String redirect = String.format("%s%s", "/", redirectUrl);
                LOGGER.debug("lang {} detected, redirect to: {}", lang, redirect);
                throw new RedirectToUrlException(redirect);
            default:
        }
    }

}
