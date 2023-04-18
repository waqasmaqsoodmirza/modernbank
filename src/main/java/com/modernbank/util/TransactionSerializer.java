package com.modernbank.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.modernbank.domain.Transaction;
import com.modernbank.domain.TransactionType;

import java.io.IOException;

public class TransactionSerializer extends JsonSerializer<Transaction> {
    @Override
    public void serialize(Transaction transaction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if(transaction.getType().equals(TransactionType.DEBIT)) {
            jsonGenerator.writeNumberField("account-id", transaction.getSenderAccount().getId());
            jsonGenerator.writeNumberField("amount", transaction.getAmount());
            jsonGenerator.writeStringField("currency", transaction.getSenderAccount().getCurrency());
            jsonGenerator.writeStringField("type", transaction.getType().toString());
            jsonGenerator.writeStringField("transaction-date", transaction.getDateTime().toString());
        } else if(transaction.getType().equals(TransactionType.CREDIT)) {
            jsonGenerator.writeNumberField("account-id", transaction.getReceiverAccount().getId());
            jsonGenerator.writeNumberField("amount", transaction.getAmount());
            jsonGenerator.writeStringField("currency", transaction.getSenderAccount().getCurrency());
            jsonGenerator.writeStringField("type", transaction.getType().toString());
            jsonGenerator.writeStringField("transaction-date", transaction.getDateTime().toString());
        }

        jsonGenerator.writeEndObject();
    }
}
