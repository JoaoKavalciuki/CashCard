package com.cashcard.cashcard;

import com.cashcard.cashcard.dto.RequestCashCardDTO;
import com.cashcard.cashcard.dto.ResponseCashCardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@JsonTest
public class CashCardJsonTest {
    @Autowired
    private JacksonTester<ResponseCashCardDTO> json;

    private ResponseCashCardDTO[] cashCardsArray = {
            new ResponseCashCardDTO(99L, 300.00, "Jason"),
            new ResponseCashCardDTO(100L, 550.00, "Jason"),
            new ResponseCashCardDTO(101L, 835.00, "Jason")
    };

    @Autowired
    private JacksonTester<ResponseCashCardDTO[]> jsonList;

    @Test
    void cashCardSerializationTest() throws IOException{
        ResponseCashCardDTO cashCard = cashCardsArray[0];

        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(300.00);
    }

    @Test
    void cashCardDeserealizationTest() throws IOException{
        String expectedValue = """
                {
                    "id": 99,
                    "amount": 123.45,
                    "owner": "Jason"
                }
                """;

        assertThat(json.parse(expectedValue)).isEqualTo(new ResponseCashCardDTO(99L, 123.45, "Jason"));

        assertThat(json.parseObject(expectedValue).id()).isEqualTo(99);
        assertThat(json.parseObject(expectedValue).amount()).isEqualTo(123.45);
    }

    @Test
    void cashCardListSerialization() throws IOException {
        assertThat(jsonList.write(cashCardsArray)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void cashCardListDeserialization() throws IOException {
        String expectedValues = """
                [
                  {
                    "id": 99,
                    "amount": 300.00,
                    "owner": "Jason"
                  },
                  {
                    "id": 100,
                    "amount": 550.00,
                    "owner": "Jason"
                  },
                  {
                    "id": 101,
                    "amount": 835.00,
                    "owner": "Jason"
                  }
                ]
                """;

        assertThat(jsonList.parseObject(expectedValues)).isEqualTo(cashCardsArray);
    }
}
