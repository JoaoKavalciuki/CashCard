package com.cashcard.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CashcardApplicationTests {
	private TestRestTemplate template = new TestRestTemplate();
	@Test
	public void returnCashCardWhenDataIsSaved(){
		String cashCardUrl = "http://localhost:8080/cashcards/99";
		ResponseEntity<String> response = template.getForEntity(cashCardUrl, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void contextLoads() {
	}

}
