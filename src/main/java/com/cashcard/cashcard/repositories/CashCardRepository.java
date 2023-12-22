package com.cashcard.cashcard.repositories;

import com.cashcard.cashcard.CashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashCardRepository extends CrudRepository<CashCard, Long> {
}
