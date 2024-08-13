package com.aruiz.microservices.order_services;

import com.aruiz.microservices.order_services.controller.dto.OrderResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import io.restassured.RestAssured;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServicesApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
				{
					"skuCode" : "MacBook air M3",
					"price" : 1800,
					"quantity" : 1
				}
				""";

		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

	@Test
	void shouldSubmitFindAllOrders() {
		String submitOrderJson1 = """
            {
                "skuCode" : "Test1",
                "price" : 22.30,
                "quantity" : 3
            }
            """;

		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson1)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201);

		String submitOrderJson2 = """
            {
                "skuCode" : "Test2",
                "price" : 222.30,
                "quantity" : 23
            }
            """;

		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson2)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201);

		// Verificar los detalles específicos de los pedidos
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/order")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()", Matchers.is(2))
				.body("[0].skuCode", Matchers.equalTo("Test1"))
				.body("[0].price", Matchers.equalTo(22.30f))
				.body("[0].quantity", Matchers.equalTo(3))
				.body("[1].skuCode", Matchers.equalTo("Test2"))
				.body("[1].price", Matchers.equalTo(222.30f))
				.body("[1].quantity", Matchers.equalTo(23));
	}
	
}
