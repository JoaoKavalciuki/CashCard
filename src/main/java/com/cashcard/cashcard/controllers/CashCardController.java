package com.cashcard.cashcard.controllers;

import com.cashcard.cashcard.CashCard;
import com.cashcard.cashcard.repositories.CashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository repository;

    @Autowired
    private CashCardController(CashCardRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> createCashCard(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {
        Optional<CashCard> cashCard = repository.findById(id);

        return cashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}