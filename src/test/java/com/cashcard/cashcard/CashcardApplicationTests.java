package com.cashcard.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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
		ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8080/cashcards", newCashCard, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newCashCardLocation = response.getHeaders().getLocation();

		ResponseEntity<String> getResponse = restTemplate.getForEntity(newCashCardLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Test
	public void notReturnCashCardWithUnknowId(){
		String cashCardUrl = "http://localhost:8080/cashcards/1000";
		ResponseEntity<String> response = restTemplate.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void contextLoads() {
	}

}
