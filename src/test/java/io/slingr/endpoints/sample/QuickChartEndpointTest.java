package io.slingr.endpoints.sample;

import io.slingr.endpoints.utils.Json;
import io.slingr.endpoints.utils.tests.EndpointTests;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class QuickChartEndpointTest {

    private static final Logger logger = LoggerFactory.getLogger(QuickChartEndpointTest.class);

    private static EndpointTests test;

    @BeforeClass
    public static void init() throws Exception {
        test = EndpointTests.start(new io.slingr.endpoints.quickchart.Runner(), "test.properties");
    }

    @Test
    public void testCreateChart() throws IOException, InterruptedException {
        final Json request = Json.fromInternalFile("createChartRequest.json");
        test.executeFunction("_chartByPost", request);
        // wait due async events
        while (true) {
            Thread.sleep(3000);
        }
    }


}
