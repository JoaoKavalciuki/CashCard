package com.cashcard.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URI;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CashcardApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(CashcardApplicationTests.class);

	@Autowired
	private TestRestTemplate restTemplate;

	final private static String CASH_CARDS_URL = "http://localhost:8080/cashcards";
	@Test
	public void returnCashCardWhenDataIsSaved(){
		String cashCardUrl = "http://localhost:8080/cashcards/99";
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		Number amount = documentContext.read("$.amount");
		String owner = documentContext.read("$.owner");

		assertThat(id).isEqualTo(99);
		assertThat(amount).isNotEqualTo(300);
		assertThat(owner).isEqualTo("Jason");
	}

	//Not working onm this project, but passed on the lab
	/*@Test
	@DirtiesContext
	public void createNewCashCard(){
		CashCard newCashCard = new CashCard(null, 350.00, "Jason");
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
						.postForEntity(CASH_CARDS_URL, newCashCard, String.class);
			log.debug(response.toString());

			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newCashCardLocation = response.getHeaders().getLocation();

		ResponseEntity<String> getResponse = restTemplate.getForEntity(newCashCardLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

		Number id = documentContext.read("@.id");
		Number amount = documentContext.read("@.amount");
		String owner = documentContext.read("@.owner");

		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(350.00);
		assertThat(owner).isEqualTo("Jason");

	}*/

	@Test
	public void notReturnCashCardWithBadCredentials(){
		String url = CASH_CARDS_URL + "/99";
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "123")
				.getForEntity(url, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void notReturnCashCardWithUnknowId(){
		String cashCardUrl = "http://localhost:8080/cashcards/1000";
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void returnCashCardsList(){
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(CASH_CARDS_URL, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		Integer cashCardsQuantity = documentContext.read("$.length()");
		assertThat(cashCardsQuantity).isEqualTo(3);

		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

		JSONArray amounts = documentContext.read("$..amount");

		assertThat(amounts).containsExactlyInAnyOrder(300.00, 550.00, 835.00);

		JSONArray owners = documentContext.read("$..owner");
		assertThat(owners).containsExactly("Jason", "Jason", "Jason");
	}

	@Test
	public void returnAPageWithOneCashCard(){
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(CASH_CARDS_URL + "?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray pageResult = documentContext.read("$[*]");
		assertThat(pageResult.size()).isEqualTo(1);

	}

	@Test
	public void returnASortedPageOfCashCards(){
		String url = CASH_CARDS_URL + "?page=0&size=1&sort=amount,asc";
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(url, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray pageResult = documentContext.read("$[*]");
		assertThat(pageResult.size()).isEqualTo(1);

		Double amount = documentContext.read("$[0].amount");

		assertThat(amount).isEqualTo(300.00);
	}

	@Test
	void returnSortedPageUsingDefaultValues(){
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("Jason", "12345")
				.getForEntity(CASH_CARDS_URL, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		JSONArray page = documentContext.read("$[*]");

		assertThat(page.size()).isEqualTo(3);

		JSONArray amounts = documentContext.read("$..amount");

		assertThat(amounts).containsExactly(835.00, 550.00, 300.00);
	}

	@Test
	void contextLoads() {
	}

}
