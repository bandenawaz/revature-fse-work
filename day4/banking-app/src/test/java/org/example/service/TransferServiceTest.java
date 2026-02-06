package org.example.service;

import org.example.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {

    private final TransferService transferService = new TransferService();

    @Test
    void shouldThrowExceptionWhenBalanceIsLow(){
        assertThrows(InsufficientBalanceException.class, () ->{
            transferService.transfer("TEST_USER","RECIEVER",99999.0);
        });

    }

    @Test
    void shouldNotThrowExceptionForValidTransfer(){
        assertDoesNotThrow(()->{
            transferService.transfer("TEST_USER","RECIEVER",99.0);
        });
    }

}