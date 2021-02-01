package webserver.ui;

import db.DataBase;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import webserver.domain.HttpRequest;
import webserver.domain.HttpResponse;

import java.io.IOException;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (isLogin(httpRequest.getParameter("Cookie"))) {
            httpResponse.forwardBody(getUserListHtml());
        }

        httpResponse.sendRedirect("/index.html");
    }

    private boolean isLogin(String cookie) {
        return cookie.contains("logined=true");
    }

    private byte[] getUserListHtml() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");

        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("inc", (value, options) -> (int) value + 1);

        try {
            Template template = handlebars.compile("user/list");
            String userList = template.apply(DataBase.findAll());

            return userList.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not compile list.html template");
        }
    }
}
