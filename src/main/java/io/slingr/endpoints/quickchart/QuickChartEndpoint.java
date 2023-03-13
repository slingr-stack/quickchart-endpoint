package io.slingr.endpoints.quickchart;

import io.slingr.endpoints.Endpoint;
import io.slingr.endpoints.HttpEndpoint;
import io.slingr.endpoints.HttpPerUserEndpoint;
import io.slingr.endpoints.exceptions.EndpointException;
import io.slingr.endpoints.framework.annotations.*;
import io.slingr.endpoints.services.AppLogs;
import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * <p>QuickChart endpoint
 * <p>
 * API Reference: https://quickchart.io/
 *
 * <p>Created by hpacini on 11/22/19.
 */
@SlingrEndpoint(name = "quickchart", functionPrefix = "_")
public class QuickChartEndpoint extends HttpEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(QuickChartEndpoint.class);

    private final static String API_URL = "https://quickchart.io";
    private final static String FORMAT_PNG = "png";
    private final static String FORMAT_PDF = "pdf";
    private final static String FORMAT_SVG = "svg";

    @EndpointProperty
    private String key;

    @ApplicationLogger
    protected AppLogs appLogger;

    @Override
    public void endpointStarted() {  }

    @Override
    public String getApiUri() { return API_URL; }

    @EndpointFunction(name = "_post")
    public Json post(FunctionRequest request) {
        Json resp = Json.map();
        Json params = request.getJsonParams();
        String path = params.string("path");
        Json body = params.json("body");

        appLogger.info("Performing request of generation of chart");

        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            appLogger.info("Waiting for the generation of the chart");
            String fileName = "chart-" + UUID.randomUUID();
            if (body.contains("name")) {
                fileName = body.string("name");
                body.remove("name");
            }
            if (StringUtils.isNotBlank(key)) {
                body.set("key", key);
            }
            // to avoid 408 responses from the API
            body.set("devicePixelRatio", 1.0);

            HttpClient httpClient = HttpClientBuilder.create().build();
            String URI = API_URL + path;
            HttpPost post = new HttpPost(URI);
            post.setHeader("Content-Type", "application/json");
            InputStream is = null;
            String status = "ok";
            int statusCode = 200;
            boolean hasFile = true;
            try {
                post.setEntity(new StringEntity(body.toString(),"UTF-8"));
                HttpResponse response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != 200) {
                    status = "fail";
                    statusCode = response.getStatusLine().getStatusCode();
                    if (response.getEntity() == null || response.getEntity().getContentType() == null ||
                            !StringUtils.equals(response.getEntity().getContentType().getValue(), "image/png")) {
                        hasFile = false;
                    }
                }
                if (hasFile) {
                    is = response.getEntity().getContent();
                    ContentType contentType = ContentType.getOrDefault(response.getEntity());
                    String mimeType = contentType.getMimeType();
                    if (body.contains("format") && body.string("format").equals(FORMAT_PDF) ||
                            body.contains("f") && body.string("f").equals(FORMAT_PDF)) {
                        fileName += "." + FORMAT_PDF;
                    } else {
                        fileName += "." + FORMAT_PNG;
                    }
                    appLogger.info(String.format("Start to upload chart [%s]", fileName));
                    Json fileJson = files().upload(fileName, is, mimeType);
                    appLogger.info(String.format("Chart was upload successfully as [%s]", fileName));
                    resp.set("file", fileJson);

                    appLogger.info("Chart created successfully");
                } else {
                    appLogger.warn(String.format("Image was not received. Status code [%s]", response.getStatusLine().getStatusCode()));
                }
            } catch (IOException e) {
                String ERROR_MESSAGE = "Error to generate chart file";
                logger.error(ERROR_MESSAGE, e);
                appLogger.error(ERROR_MESSAGE, e);
                status = "error";
                statusCode = 500;
            } finally {
                resp.set("status", status);
                resp.set("statusCode", statusCode);
                events().send("chartResponse", resp, request.getFunctionId());
                IOUtils.closeQuietly(is);
            }
        });
        return Json.map().set("status", "ok");
    }

    @EndpointFunction(name = "_get")
    public Json get(FunctionRequest request) {
        Json resp;
        try {
            resp = defaultGetRequest(request);

            InputStream is = new ByteArrayInputStream(resp.toString().getBytes());
            ContentType contentType = ContentType.DEFAULT_TEXT;
            String mimeType = contentType.getMimeType();
            String extension = "." + FORMAT_PNG;
            Json fileJson = files().upload("qr-" + UUID.randomUUID() + extension, is, mimeType);

            resp.set("status", "ok");
            resp.set("file", fileJson);

            events().send("qrResponse", resp, request.getFunctionId());
            IOUtils.closeQuietly(is);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Json.map();
            resp.set("status", "fail");
        }
        return resp;
    }
}