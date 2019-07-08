package tweets;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Tweets {
	public String consumerKey = "dG5c902BvvjqsGLEzbixrtMyw";
	public String consumerSecret = "gSfUhzKrIybKhUTUZjklkYzfyfnF9sqhMS4c1koQgdiFoYfLqt";
	public String accessToken = "487303104-1hQbN3y76lInpXdnipFkrprJZX4d7EU7yIeOvWcX";
	public String secretToken = "BM53v4bMrLqFWtMgmTATd2TRiajCKk1hEcqcoxiKVtPI5";
	String id;

	@Test(priority=2, enabled= true, groups="E2E")
	public void getLatestTweet() {
		RestAssured.baseURI = "https://api.twitter.com/1.1/statuses";
		Response res = given().
				auth().oauth(consumerKey, consumerSecret, accessToken, secretToken)
				.queryParam("count", 1).
				when().
				get("/home_timeline.json").
				then().assertThat().statusCode(200).extract().response();

		System.out.println(res.asString());
		String jsonData = res.asString();
		JsonPath js = new JsonPath(jsonData);
		String aId= js.getString("id").toString();
		System.out.println(aId);
		System.out.println(js.getString("id"));
		//Assert.assertEquals(id, aId);
	}

	@Test(enabled= true,priority=1, groups= {"E2E","smoke"})
	public void postTweet() throws InterruptedException {
		RestAssured.baseURI = "https://api.twitter.com/1.1/statuses";
		Response res = given()
				.auth().oauth(consumerKey, consumerSecret, accessToken, secretToken).queryParam("status", "Hello Twitter")
				.when()
				.post("update.json")
				.then()
				.assertThat().statusCode(200).extract().response();

		String jsonData = res.asString();
		JsonPath js = new JsonPath(jsonData);
		id= js.getString("id").toString();
		System.out.println("ID is:"+id);
		System.out.println(js.getString("id"));
		Thread.sleep(10000);
	}
	@Test(enabled= false, priority=3,groups= {"E2E", "smoke"})
	public void deleteTweet() {
		RestAssured.baseURI = "https://api.twitter.com/1.1/statuses";
		Response res = given().
				auth()
				.oauth(consumerKey, consumerSecret, accessToken, secretToken).
				
				when()
				.post("/destroy/"+id+".json").
				
				then().
				assertThat().statusCode(200).extract().response();
		
		String jsonData = res.asString();
		JsonPath js = new JsonPath(jsonData);
		System.out.println(js.getString("id"));

	}
}
