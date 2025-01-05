package info.saladlam.example.spring.noticeboard.wicket.system;

import info.saladlam.example.spring.noticeboard.ApplicationConstant;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

import java.util.List;

public class ChangeLanguageRequestMapper implements IRequestMapper {

    @Override
    public IRequestHandler mapRequest(Request request) {
        List<Url.QueryParameter> parameters = request.getUrl().getQueryParameters();
        for (Url.QueryParameter p : parameters) {
            if (ApplicationConstant.CHANGE_LOCALE_PARAMETER.equals(p.getName())) {
                return new ChangeLanguageRequestHandler();
            }
        }
        return null;
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 10000;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        return null;
    }

}
