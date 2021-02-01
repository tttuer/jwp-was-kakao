package webserver.domain;

import annotation.web.ResponseMethod;
import com.google.common.collect.Maps;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {
    private static final String rootDirectory = "./templates";
    private Map<String, String> headers = Maps.newHashMap();
    private DataOutputStream dos;

    public HttpResponse(OutputStream os) {
        dos = new DataOutputStream(os);
    }

    public void forward(String path) {
        makeResponse(ResponseMethod.OK);

        try {
            addHeadersAndSend(FileIoUtils.loadFileFromClasspath(rootDirectory + path));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("");
        }
    }

    public void forwardBody(byte[] body) {
        makeResponse(ResponseMethod.OK);
        addHeadersAndSend(body);
    }

    public void sendRedirect(String path) {
        makeResponse(ResponseMethod.FOUND);

        try {
            addHeadersAndSend(FileIoUtils.loadFileFromClasspath(rootDirectory + path));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("");
        }
    }

    private void addHeadersAndSend(byte[] body) {
        String resultHeaders = headers.keySet()
                .stream()
                .map(key -> String.format("%s: %s", key, headers.get(key)))
                .collect(Collectors.joining("\r\n"));

        try {
            dos.writeBytes(resultHeaders);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("asd");
        }
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private void makeResponse(ResponseMethod responseMethod) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %d %s",
                    responseMethod.getStatusCode(), responseMethod.getMessage()));
        } catch (IOException e) {
            throw new IllegalArgumentException("asdfasdf");
        }
    }
}
