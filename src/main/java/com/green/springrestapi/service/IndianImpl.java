package com.green.springrestapi.service;


import org.springframework.stereotype.Service;

@Service
public class IndianImpl implements Bank {
    @Override
    public void pay() {
        System.out.println("Payment from IOB...");
    }
}
