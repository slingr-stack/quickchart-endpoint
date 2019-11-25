package io.slingr.endpoints.quickchart;

import io.slingr.endpoints.Endpoint;
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
public class QuickChartEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(QuickChartEndpoint.class);

    private final static String API_URL = "https://quickchart.io";
    private final static String FORMAT_PNG = "png";
    private final static String FORMAT_PDF = "pdf";
    private final static String FORMAT_SVG = "svg";

    @ApplicationLogger
    protected AppLogs appLogger;

    @Override
    public void endpointStarted() {
    }

    @EndpointFunction(name = "_chartByPost")
    public Json chartByPost(FunctionRequest request) {

        Json resp = Json.map();

        Json params = request.getJsonParams();
        String path = params.string("path");
        Json body = params.json("body");

        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            StringEntity entity = new StringEntity(body.toString(), ContentType.APPLICATION_FORM_URLENCODED);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost req = new HttpPost(API_URL + path);
            req.setHeader("Content-Type", "application/json");
            req.setEntity(entity);

            HttpResponse response = null;
            try {

                response = httpClient.execute(req);

                if(response.getStatusLine().getStatusCode() != 200) {
                    resp.set("status", "fail");
                    resp.set("statusCode", response.getStatusLine().getStatusCode());

                    events().send("chartResponse", resp, request.getFunctionId());
                    return;
                }

                InputStream is = response.getEntity().getContent();

                ContentType contentType = ContentType.getOrDefault(response.getEntity());
                String mimeType = contentType.getMimeType();

                String extension = "." + FORMAT_PNG;
                if (body.contains("format") && body.string("format").equals(FORMAT_PDF) ||
                        body.contains("f") && body.string("f").equals(FORMAT_PDF)) {
                    extension = "." + FORMAT_PDF;
                }

                Json fileJson = files().upload("chart-" + UUID.randomUUID() + extension, is, mimeType);

                resp.set("status", "ok");
                resp.set("file", fileJson);

                events().send("chartResponse", resp, request.getFunctionId());

                IOUtils.closeQuietly(is);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return Json.map().set("status", "ok");
    }

    @EndpointFunction(name = "_qrByGet")
    public Json qrByGET(FunctionRequest request) {

        Json resp = Json.map();

        Json params = request.getJsonParams();
        String path = params.string("path");

        Executors.newSingleThreadScheduledExecutor().execute(() -> {

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet req = new HttpGet(API_URL + path);
            req.setHeader("Content-Type", "application/json");

            HttpResponse response = null;
            try {

                response = httpClient.execute(req);

                if(response.getStatusLine().getStatusCode() != 200) {
                    resp.set("status", "fail");
                    resp.set("statusCode", response.getStatusLine().getStatusCode());

                    events().send("qrResponse", resp, request.getFunctionId());
                    return;
                }

                InputStream is = response.getEntity().getContent();

                ContentType contentType = ContentType.getOrDefault(response.getEntity());
                String mimeType = contentType.getMimeType();

                String extension = "." + FORMAT_PNG;
                if (StringUtils.contains(path, "format=" + FORMAT_SVG)) {
                    extension = "." + FORMAT_SVG;
                }

                Json fileJson = files().upload("qr-" + UUID.randomUUID() + extension, is, mimeType);

                resp.set("status", "ok");
                resp.set("file", fileJson);

                events().send("qrResponse", resp, request.getFunctionId());

                IOUtils.closeQuietly(is);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return Json.map().set("status", "ok");
    }

}
