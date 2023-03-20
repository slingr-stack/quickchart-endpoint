package io.slingr.endpoints.quickchart;

import io.slingr.endpoints.HttpEndpoint;
import io.slingr.endpoints.framework.annotations.*;
import io.slingr.endpoints.services.AppLogs;
import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * <p>QuickChart endpoint
 * <p>
 * API Reference: <a href="https://quickchart.io/">Quickchart</a>
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
        String path = params.string("path");
        Json body = params.json("body");

        appLogger.info("Performing request of generation of chart");

        String fileName = "chart-" + UUID.randomUUID();
        fileName = getFormat(body, fileName, FORMAT_PDF);
        if (StringUtils.isNotBlank(key)) {
            body.set("key", key);
        }
        // to avoid 408 responses from the API
        body.set("devicePixelRatio", 1.0);

        resp = defaultPostRequest(request);

        InputStream is = null;
        try {
            if (path.contains("/chart")) {
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

    private String getFormat(Json body, String fileName, String anotherFormat) {
        if (body.contains("format") && body.string("format").equals(anotherFormat) ||
                body.contains("f") && body.string("f").equals(anotherFormat)) {
            fileName += "." + anotherFormat;
        } else {
            fileName += "." + FORMAT_PNG;
        }
        if (body.contains("name")) {
            fileName = body.string("name");
            body.remove("name");
        }
        return fileName;
    }

    @EndpointFunction(name = "_get")
    public Json get(FunctionRequest request) {
        Json resp = Json.map();
        Json params = request.getJsonParams();
        String path = params.string("path");
        Json body = params.json("body");
        String fileName = "qr-" + UUID.randomUUID();
        fileName = getFormat(body, fileName, FORMAT_SVG);

        appLogger.info("Performing request of generation of qr");

        try {
            resp = defaultGetRequest(request);

            if (path.contains("/qr")) {
                InputStream is = new ByteArrayInputStream(resp.toString().getBytes());
                ContentType contentType = ContentType.DEFAULT_TEXT;
                String mimeType = contentType.getMimeType();
                appLogger.info(String.format("Start to upload qr [%s]", fileName));
                Json fileJson = files().upload(fileName, is, mimeType);
                appLogger.info(String.format("Qr was upload successfully as [%s]", fileName));

                resp.set("status", "ok");
                resp.set("file", fileJson);

                events().send("qrResponse", resp, request.getFunctionId());
                IOUtils.closeQuietly(is);
                appLogger.info("Qr created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.set("status", "fail");
        }
        return resp;
    }
}