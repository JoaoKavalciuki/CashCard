package com.cashcard.cashcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
public class CashCardJsonTest {
    @Autowired
    private JacksonTester<CashCard> json;

    private ArrayList<CashCard> cashCards = new ArrayList<>();

    @Autowired
    private JacksonTester<ArrayList<CashCard>> jsonList;

    @BeforeEach
    void dataSetUp(){
        cashCards.add(new CashCard(99L, 300.00));
        cashCards.add(new CashCard(100L, 550.00));
        cashCards.add(new CashCard(101L, 830.00));
    }


    @Test
    void cashCardSerializationTest() throws IOException{
        CashCard cashCard = new CashCard(99L, 123.45);

        assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
    }

    @Test
    void cashCardDeserealizationTest() throws IOException{
        String expectedValue = """
                {
                    "id": 99,
                    "amount": 123.45
                }
                """;

        //assertThat(json.parse(expectedValue)).isEqualTo(new CashCard(99L, 123.45));

        assertThat(json.parseObject(expectedValue).getId()).isEqualTo(99);
        assertThat(json.parseObject(expectedValue).getAmount()).isEqualTo(123.45);
    }

    @Test
    void cashCardListSerialization() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("list.json");
    }
}
