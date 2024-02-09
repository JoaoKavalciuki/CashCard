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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.security.Principal;
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
    public ResponseEntity<Void> createCashCard(@RequestBody RequestCashCardDTO newCashCard, UriComponentsBuilder ucb, Principal user){
        CashCard savedCashCard = new CashCard(newCashCard, user);
        repository.save(savedCashCard);

        URI newCashCardLocation = ucb.path("/cashcards/{id}").buildAndExpand(savedCashCard.getId()).toUri();

        return  ResponseEntity.created(newCashCardLocation).build();
    }
    @GetMapping
    public ResponseEntity<List<ResponseCashCardDTO>> findAll(Pageable pageable, Principal owner){

        Page<CashCard> cashCardsList = repository.findAllByOwner(
                owner.getName(),
                PageRequest.of(
                        pageable.getPageNumber(), pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "amount"))
                )
        );

     List<ResponseCashCardDTO> cashCardDTOS = cashCardsList
                .map(cashCard -> new ResponseCashCardDTO(cashCard.getId(), cashCard.getAmount(), cashCard.getOwner()))
                .stream().toList();


        return ResponseEntity.ok(cashCardDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCashCardDTO> findById(@PathVariable Long id, Principal owner) {
        Optional<CashCard> cashCardOptional = Optional.ofNullable(repository.findCashCardByIdAndOwner(id, owner.getName()));

        if(cashCardOptional.isPresent()){
            CashCard cashCard = cashCardOptional.get();
            ResponseCashCardDTO cashCardDTO =  new ResponseCashCardDTO(cashCard.getId(), cashCard.getAmount(), cashCard.getOwner());

            return ResponseEntity.ok(cashCardDTO);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCashCard(@RequestBody ResponseCashCardDTO updateCashCardDTO, @PathVariable Long id, Principal user){
        Optional<CashCard> cashCardToUpdateOptional = Optional.ofNullable(repository.findCashCardByIdAndOwner(id, user.getName()));

        if(cashCardToUpdateOptional.isPresent()){
            CashCard cashCardToUpdate = cashCardToUpdateOptional.get();

            cashCardToUpdate.setAmount(updateCashCardDTO.amount());

            repository.save(cashCardToUpdate);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}