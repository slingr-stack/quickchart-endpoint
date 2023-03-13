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
        Json resp;
        Json params = request.getJsonParams();
        Json body = params.json("body");
        if (!body.string("isDefaultCall").equals("true")) {
            return defaultPostRequest(request);
        }

        appLogger.info("Performing request of generation of chart");

        String fileName = "chart-" + UUID.randomUUID();
        String extension = "." + FORMAT_PNG;
        if (body.contains("name")) {
            fileName = body.string("name");
            body.remove("name");
        }
        fileName = fileName + extension;

        if (StringUtils.isNotBlank(key)) {
            body.set("key", key);
        }
        // to avoid 408 responses from the API
        body.set("devicePixelRatio", 1.0);

        resp = defaultPostRequest(request);

        InputStream is = null;
        try {

            is = new ByteArrayInputStream(resp.toString().getBytes());
            ContentType contentType = ContentType.DEFAULT_TEXT;
            String mimeType = contentType.getMimeType();
            appLogger.info(String.format("Start to upload chart [%s]", fileName));
            Json fileJson = files().upload(fileName, is, mimeType);
            appLogger.info(String.format("Chart was upload successfully as [%s]", fileName));
            resp.set("file", fileJson);

            resp.set("status", "ok");
            resp.set("file", fileJson);

            events().send("chartResponse", resp, request.getFunctionId());
            appLogger.info("Chart created successfully");
        }
        catch (Exception e) {
            String ERROR_MESSAGE = "Error to generate chart file";
            logger.error(ERROR_MESSAGE, e);
            appLogger.error(ERROR_MESSAGE, e);
            resp.set("status", "fail");
        }
        finally {
            IOUtils.closeQuietly(is);
        }
        return resp;
    }

    @EndpointFunction(name = "_get")
    public Json get(FunctionRequest request) {
        Json resp = Json.map();
        Json params = request.getJsonParams();
        Json body = params.json("body");
        if (!body.string("isDefaultCall").equals("true")) {
            return defaultGetRequest(request);
        }

        appLogger.info("Performing request of generation of qr");

        try {
            resp = defaultGetRequest(request);

            InputStream is = new ByteArrayInputStream(resp.toString().getBytes());
            ContentType contentType = ContentType.DEFAULT_TEXT;
            String mimeType = contentType.getMimeType();
            String extension = "." + FORMAT_PNG;
            String fileName = "qr-" + UUID.randomUUID() + extension;
            appLogger.info(String.format("Start to upload qr [%s]", fileName));
            Json fileJson = files().upload(fileName, is, mimeType);
            appLogger.info(String.format("Qr was upload successfully as [%s]", fileName));

            resp.set("status", "ok");
            resp.set("file", fileJson);

            events().send("qrResponse", resp, request.getFunctionId());
            IOUtils.closeQuietly(is);
            appLogger.info("Qr created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            resp.set("status", "fail");
        }
        return resp;
    }
}