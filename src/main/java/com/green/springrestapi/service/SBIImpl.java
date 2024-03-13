package com.green.springrestapi.service;

import org.springframework.stereotype.Service;

@Service
public class SBIImpl implements Bank {
    @Override
    public void pay() {
        System.out.println("Payment from SBI...");
    }
}
