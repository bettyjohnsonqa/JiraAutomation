import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.filter.session.SessionFilter;

public class AddAttachmentApi {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8080";
		//Login Cookie Authentication api
		SessionFilter session = new SessionFilter();
		given().log().all().header("Content-Type","application/json").body("{ \"username\": \"bettyjohnsonqa\", \"password\": \"Automation@2023\" }")
		.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		//Add Attachment
		given().log().all().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10008")
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("JiraAttach.txt")).when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
		

	}

}
