package in.clouthink.synergy.test.controller;

import in.clouthink.synergy.test.common.AbstractTest;
import org.junit.Test;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class ActivityRestControllerTest extends AbstractTest {

    @Test
    public void testList() throws Exception {
        // ActivityView.class
        given()
                .get("/api/activities")
                .then()
                .assertThat()
                .statusCode(200);
    }

}
