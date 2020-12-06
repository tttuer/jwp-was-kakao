package webserver;

import controller.Controller;
import domain.HttpHeader;
import domain.HttpRequest;
import domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequstParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest httpRequest = new HttpRequstParser(bufferedReader).getHttpRequest();
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos, httpRequest);
            route(httpRequest, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void route(HttpRequest request, HttpResponse response) {
        ControllerRouter router = new ControllerRouter(request);
        Controller controller = router.get();
        controller.execute(request, response);
    }

}
