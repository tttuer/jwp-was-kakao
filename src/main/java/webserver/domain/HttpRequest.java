package webserver.domain;

import annotation.web.RequestMethod;
import com.google.common.collect.Maps;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Stream;

public class HttpRequest {
    private RequestMethod requestMethod;
    private String path;
    private String httpVersion;
    private final Map<String, String> headers = Maps.newHashMap();
    private final Map<String, String> parameters = Maps.newHashMap();

    public HttpRequest(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] token = getNextLine(br).split(" ");

        checkValidRequest(token);

        requestMethod = RequestMethod.of(token[0]);
        String[] uri = token[1].split("\\?");
        String queryString = (uri.length > 1) ? uri[1] : "";
        path = uri[0];
        httpVersion = token[2];

        makeHeaders(br);
        makeParameters(queryString);
        makeParameters(getBodyData(br));
    }

    private void checkValidRequest(String[] token) {
        if (token.length < 3) {
            throw new IllegalArgumentException("Incorrect request");
        }
    }

    private void makeHeaders(BufferedReader br) {
        for (String line = getNextLine(br); !"".equals(line) && line != null; line = getNextLine(br)) {
            String[] tokens = line.split(":");
            headers.put(tokens[0].trim(), tokens[1].trim());
        }
    }

    private void makeParameters(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }

        Stream.of(line.split("&"))
                .forEach(pairValue -> {
                    String[] pair = pairValue.split("=");
                    parameters.put(pair[0], pair[1]);
                });
    }

    private String getNextLine(BufferedReader br) {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private String getBodyData(BufferedReader br) {
        try {
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            return IOUtils.readData(br, contentLength);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String getMethod() {
        return requestMethod.toString();
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String header) {
        return headers.getOrDefault(header, "");
    }

    public String getParameter(String parameter) {
        return parameters.getOrDefault(parameter, "");
    }
}
