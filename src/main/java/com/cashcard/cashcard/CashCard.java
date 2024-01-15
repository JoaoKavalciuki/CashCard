package com.cashcard.cashcard;


import com.cashcard.cashcard.dto.RequestCashCardDTO;
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

   String owner;

   public  CashCard(){

   }

   public CashCard(Long id, Double amount, String owner){
       this.id = id;
       this.amount = amount;
       this.owner = owner;
   }

   public CashCard(RequestCashCardDTO cashCardDTO){
       this.amount = cashCardDTO.amount();
       this.owner = cashCardDTO.owner();
   }

   public Long getId(){
       return this.id;
   }

   public Double getAmount(){
       return this.amount;
   }

   public String getOwner(){
       return this.owner;
   }

   public void setAmount(Double amount){
       this.amount = amount;
   }
}
