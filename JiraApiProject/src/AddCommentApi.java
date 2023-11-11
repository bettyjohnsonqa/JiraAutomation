import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

import  static io.restassured.RestAssured.*;

public class AddCommentApi {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";
		//Login Cookie Authentication api
		SessionFilter session = new SessionFilter();
		
		given().log().all().header("Content-Type","application/json").body("{ \"username\": \"bettyjohnsonqa\", \"password\": \"Automation@2023\" }")
		.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		//Add comment API
		
		given().log().all().pathParam("id", "10008").header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \"comments added in automation addcomment api\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment")
		.then().log().all().assertThat().statusCode(201);
	}

}
