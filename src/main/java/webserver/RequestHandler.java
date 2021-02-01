package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Request;
import utils.FileIoUtils;
import utils.IOUtils;
import webserver.domain.HttpRequest;
import webserver.domain.HttpResponse;
import webserver.ui.Controller;
import webserver.ui.CreateUserController;
import webserver.ui.ListUserController;
import webserver.ui.LoginController;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final Map<String, Controller> controllers = new HashMap<>();

    public RequestHandler() {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/list", new ListUserController());
        controllers.put("/user/login", new LoginController());
    }

    public RequestHandler(Socket connectionSocket) {
        this();
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);

            controllers.get(httpRequest.getPath()).service(httpRequest, new HttpResponse(out));

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
