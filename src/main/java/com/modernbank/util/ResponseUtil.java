package com.modernbank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.modernbank.domain.Transaction;

import java.util.List;

public class ResponseUtil {
    public static String getTransactionsAsString(List<Transaction> transactions) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Transaction.class, new TransactionSerializer());
        objectMapper.registerModule(simpleModule);
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transactions);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
