package io.slingr.endpoints.quickchart;

import io.slingr.endpoints.HttpEndpoint;
import io.slingr.endpoints.framework.annotations.*;
import io.slingr.endpoints.services.AppLogs;
import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.ws.exchange.FunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>QuickChart endpoint
 * <p>
 * API Reference:
 *
 * <p>Created by hpacini on 11/22/19.
 */
@SlingrEndpoint(name = "quickchart", functionPrefix = "_")
public class QuickChartEndpoint extends HttpEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(QuickChartEndpoint.class);

    private final static String API_URL = "";

    @ApplicationLogger
    protected AppLogs appLogger;

    @Override
    public String getApiUri() {
        return API_URL;
    }

    @Override
    public void endpointStarted() {
        httpService().setupDefaultHeader("Content-Type", "application/json");
        httpService().setAllowExternalUrl(true);
        httpService().setDebug(true);
    }

    @EndpointFunction(name = "_get")
    public Json userGet(FunctionRequest request) {
        return Json.map();
    }

    @EndpointFunction(name = "_post")
    public Json userPost(FunctionRequest request) {
        return Json.map();
    }

}
