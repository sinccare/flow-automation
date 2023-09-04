package base;

import enums.Servico;
import io.restassured.response.ValidatableResponse;

import static base.RequestSpecificationFactory.requestSpecification;
import static base.RequestSpecificationFactory.responseSpecification;
import static io.restassured.RestAssured.given;

public class BaseRequest {

    public ValidatableResponse getInventory(String user, String password, String startDate, String endDate, String last_code){
        return given()
                .spec(requestSpecification(Servico.UPA_CUMBICA))
                .auth()
                .preemptive()
                .basic(user, password)
                .header("start_date", startDate)
                .header("end_date", endDate)
                .header("last_id", last_code)
                .when()
                .get("/inventory_movement")
                .then()
                .spec(responseSpecification());
    }
}
