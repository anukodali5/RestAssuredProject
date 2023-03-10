package apiChaining;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {

	Response response;

	String BaseURI = "http://localhost:3000/employees/";

	@Test
	public void test1() {

		response = GetMethodAll();
		Assert.assertEquals(response.getStatusCode(), 200);

		response = PostMethod("New1", "2000");
		Assert.assertEquals(response.getStatusCode(), 201);
		JsonPath jpath = response.jsonPath();
		int Emp_Id = jpath.get("id");
		System.out.println("id:   " + Emp_Id);

		response = Putmethod(Emp_Id, "New3", "6000");
		Assert.assertEquals(response.getStatusCode(), 200);
		jpath = response.jsonPath();
		Assert.assertEquals(jpath.get("name"), "New3");

		response = DeleteMethod(Emp_Id);
		Assert.assertEquals(response.getStatusCode(), 200);

		response = GetMethod(Emp_Id);
		Assert.assertEquals(response.getStatusCode(), 404);
		Assert.assertEquals(response.getBody().asString(), "{}");
	}

	public Response GetMethodAll() {

		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		return response;

	}

	public Response PostMethod(String Name, String Salary) {
		RestAssured.baseURI = BaseURI;
		JSONObject jobj = new JSONObject();
		jobj.put("name", Name);
		jobj.put("salary", Salary);

		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.post("/create");
		return response;

	}

	public Response Putmethod(int EmpId, String Name, String Salary) {
		RestAssured.baseURI = BaseURI;
		JSONObject jobj = new JSONObject();
		jobj.put("name", Name);
		jobj.put("salary", Salary);

		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(jobj.toString())
				.put("/" + EmpId);
		return response;

	}

	public Response DeleteMethod(int EmpId) {
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/" + EmpId);
		return response;
	}

	public Response GetMethod(int EmpId) {
		RestAssured.baseURI = BaseURI;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + EmpId);
		return response;
	}
}
