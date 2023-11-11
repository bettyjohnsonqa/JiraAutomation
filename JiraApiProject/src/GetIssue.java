import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class GetIssue {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";
		//Login Cookie Authentication api
		SessionFilter session = new SessionFilter();
		
		given().log().all().relaxedHTTPSValidation().header("Content-Type","application/json").body("{ \"username\": \"bettyjohnsonqa\", \"password\": \"Automation@2023\" }")
		.filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		//Add comment API
		String expectedComment = "comments in add comment API";
		String commentResponse = given().log().all().pathParam("id", "10008").header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \""+expectedComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(commentResponse);
		String commentId = js.getString("id");
		
		//Add Attachment
				given().log().all().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10008")
				.header("Content-Type","multipart/form-data")
				.multiPart("file",new File("JiraAttach.txt")).when().post("/rest/api/2/issue/{id}/attachments")
				.then().log().all().assertThat().statusCode(200);
				
				//get issue
				String issueDetails = given().log().all().filter(session).pathParam("id", "10008")
						.queryParam("fields", "comment")
				.when().get("/rest/api/2/issue/{id}")
				.then().extract().response().asString();
				System.out.println(issueDetails);
				JsonPath js1 = new JsonPath(issueDetails);
				int commentCount = js1.getInt("fields.comment.comments.size()");
				System.out.println(commentCount);
				for(int i = 0 ; i< commentCount ; i++)
				{
					String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
					if(commentIdIssue.equalsIgnoreCase(commentId))
					{
						String ActualComment = js1.get("fields.comment.comments["+i+"].body");
						System.out.println(ActualComment);
						Assert.assertEquals(ActualComment, expectedComment);
					}
				}
		

	}

}
