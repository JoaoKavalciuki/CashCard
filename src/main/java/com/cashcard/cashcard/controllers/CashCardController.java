package com.cashcard.cashcard.controllers;

import com.cashcard.cashcard.CashCard;

import com.cashcard.cashcard.dto.RequestCashCardDTO;
import com.cashcard.cashcard.dto.ResponseCashCardDTO;
import com.cashcard.cashcard.repositories.CashCardRepository;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.spel.spi.Function;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;
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
    public ResponseEntity<Void> createCashCard(@RequestBody RequestCashCardDTO newCashCard, UriComponentsBuilder ucb){
        CashCard savedCashCard = new CashCard(newCashCard);
        repository.save(savedCashCard);

        URI newCashCardLocation = ucb.path("/cashcards/{id}").buildAndExpand(savedCashCard.getId()).toUri();

        return  ResponseEntity.created(newCashCardLocation).build();
    }
    @GetMapping
    public ResponseEntity<List<ResponseCashCardDTO>> findAll(Pageable pageable){

        var results = repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(), pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "amount"))
                )
        );


        List<ResponseCashCardDTO> cashCardsList = results.stream().map(cashCard ->
                new ResponseCashCardDTO(cashCard.getId(), cashCard.getAmount(), cashCard.getOwner())).toList();

        return ResponseEntity.ok(cashCardsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {
        Optional<CashCard> cashCard = repository.findById(id);

        return cashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}