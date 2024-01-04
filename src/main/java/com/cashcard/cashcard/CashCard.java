package com.cashcard.cashcard;

import com.cashcard.cashcard.dto.CashCardDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class CashCard {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   Double amount;

   public  CashCard(){

   }

   public CashCard(Long id, Double amount){
       this.id = id;
       this.amount = amount;
   }

   public CashCard(CashCardDTO cashCardDTO){
       this.amount = cashCardDTO.amount();
   }

   public Long getId(){
       return this.id;
   }

   public Double getAmount(){
       return this.amount;
   }

   public void setAmount(Double amount){
       this.amount = amount;
   }
}
