package com.cashcard.cashcard.controllers;

import com.cashcard.cashcard.CashCard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(){
        CashCard cashCard = new CashCard(99L, 300.00);
        return ResponseEntity.ok(cashCard);
    }
}
