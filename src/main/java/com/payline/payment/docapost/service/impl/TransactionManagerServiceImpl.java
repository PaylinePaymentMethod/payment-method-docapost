package com.payline.payment.docapost.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.payline.payment.docapost.bean.PaymentResponseSuccessAdditionalData;
import com.payline.pmapi.service.TransactionManagerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TransactionManagerServiceImpl implements TransactionManagerService {

    private static final Logger LOGGER = LogManager.getLogger(TransactionManagerServiceImpl.class);

    /**
     * Permet de lire les additional data
     *
     * @param additionalDataJson addition data to read in Json format
     * @param version            version
     * @return Map <String,String> les additionalData  à l'interieur de la Map
     */
    @Override
    public Map<String, String> readAdditionalData(String additionalDataJson, String version) {
        Map<String, String> additionalDataMap = new HashMap<>();
        Gson gson = new Gson();

        if (null != additionalDataJson) {
            try {
                PaymentResponseSuccessAdditionalData paymentResponseSuccessAdditionalData = gson.fromJson(additionalDataJson, PaymentResponseSuccessAdditionalData.class);
                additionalDataMap.put("mandateRum", paymentResponseSuccessAdditionalData.getMandateRum());
                additionalDataMap.put("transactionId", paymentResponseSuccessAdditionalData.getTransactionId());
                additionalDataMap.put("signatureId", paymentResponseSuccessAdditionalData.getSignatureId());

            } catch (JsonSyntaxException e) {
                LOGGER.error("Additional data syntax incorrect [{}]", e.getMessage(), e);
                throw new JsonSyntaxException("Additional data syntax incorrect [{}]", e);

            }
        }
        return additionalDataMap;
    }


}
