package com.cashcard.cashcard.repositories;

import com.cashcard.cashcard.CashCard;
import com.cashcard.cashcard.dto.ResponseCashCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashCardRepository extends JpaRepository<CashCard, Long> {
    CashCard findCashCardByOwner(Long id, String owner);

    Page<CashCard> findAllByOwner(String owner, PageRequest pageRequest);
}
