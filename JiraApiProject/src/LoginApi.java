import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class LoginApi {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";
		//Login Cookie Authentication api
		SessionFilter session = new SessionFilter();

	}

}
