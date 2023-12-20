package com.cashcard.cashcard.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id){
        return ResponseEntity.ok("{}");
    }
}
