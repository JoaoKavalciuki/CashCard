package com.cashcard.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CashcardApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	final private static String CASH_CARDS_URL = "http://localhost:8080/cashcards";
	@Test
	public void returnCashCardWhenDataIsSaved(){
		String cashCardUrl = "http://localhost:8080/cashcards/99";
		ResponseEntity<String> response = restTemplate.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		Number amount = documentContext.read("$.amount");

		assertThat(id).isEqualTo(99);
		assertThat(amount).isNotEqualTo(300);
	}

	@Test
	public void createNewCashCard(){
		CashCard newCashCard = new CashCard(null, 350.00);
		ResponseEntity<Void> response = restTemplate.postForEntity(CASH_CARDS_URL, newCashCard, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newCashCardLocation = response.getHeaders().getLocation();

		ResponseEntity<String> getResponse = restTemplate.getForEntity(newCashCardLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

		Number id = documentContext.read("@.id");
		Number amount = documentContext.read("@.amount");

		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(350.00);

	}

	@Test
	public void notReturnCashCardWithUnknowId(){
		String cashCardUrl = "http://localhost:8080/cashcards/1000";
		ResponseEntity<String> response = restTemplate.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void returnCashCardsList(){
		ResponseEntity<String> response = restTemplate.getForEntity(CASH_CARDS_URL, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		Integer cashCardsQuantity = documentContext.read("$.length()");
		assertThat(cashCardsQuantity).isEqualTo(3);

		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

		JSONArray amounts = documentContext.read("$..amount");

		assertThat(amounts).containsExactlyInAnyOrder(300.00, 550.00, 835.00);
	}

	@Test
	public void returnAPageWithOneCashCard(){
		ResponseEntity<String> response = restTemplate.getForEntity(CASH_CARDS_URL + "?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray pageResult = documentContext.read("$[*]");
		assertThat(pageResult.size()).isEqualTo(1);

	}

	@Test
	void contextLoads() {
	}

}
