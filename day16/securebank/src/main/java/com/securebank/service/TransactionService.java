package com.securebank.service;

import com.securebank.model.AuditLogger;
import com.securebank.model.TransactionLedger;

public class TransactionService {
    private TransactionLedger ledger;
    private AuditLogger  auditLogger;

    public TransactionService(TransactionLedger ledger, AuditLogger auditLogger) {
        this.ledger = ledger;
        this.auditLogger = auditLogger;

        System.out.println("TransactionService created");
    }

    public void execute(String from, String to, double amount, String type) {
        auditLogger.log("TX_START", type+" from "+from+" to "+to);
        ledger.recordTransaction(from, to, amount);
        auditLogger.log("TX_DONE", "Amount ="+amount +" STATUS = OK");
    }
}
