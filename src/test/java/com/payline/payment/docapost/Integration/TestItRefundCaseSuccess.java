package com.payline.payment.docapost.Integration;

import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseSuccess;
import org.junit.jupiter.api.Assertions;

public class TestItRefundCaseSuccess extends AbstractTestItRefundCase {

    private static final String SCT_ORDER_CREATED_STATUS__CREATED = "CREATED";

    public static void main(String[] args) {

        // To get a refund action with success response, the transaction id must be unique
        generateTransactionId();

        main();

        //**************************************************************************************************************
        // SCT ORDER CREATE for refund

        // Refund Request :
        // => API MandateWS /api/sctorder/create

        // Create the RefundRequest, execute the request and get the response result
        RefundRequest refundRequest = createRefundRequest(REFUND_ADDITIONAL_DATA, REFUND_PARTNER_TRANSACTION_ID);
        RefundResponse refundResponseFromRefundRequest = TEST_IT.refundService.refundRequest(refundRequest);

        // ... check the response (should be a RefundResponseSuccess)
        checkRefundResponse(refundResponseFromRefundRequest);

    }

    /**
     * Generate a unique transaction id
     */
    private static void generateTransactionId() {
        long randomNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        TRANSACTION_ID = String.valueOf(randomNumber);
    }

    /**
     * Check the refund request response
     *
     * @param refundResponse
     */
    private static void checkRefundResponse(RefundResponse refundResponse) {

        TEST_IT.checkRefundResponseIsNotFailure(refundResponse);
        TEST_IT.checkRefundResponseIsRightClass("refundRequest", refundResponse, RefundResponseSuccess.class);

        RefundResponseSuccess refundResponseSuccess = (RefundResponseSuccess) refundResponse;

        Assertions.assertEquals(TRANSACTION_ID, refundResponseSuccess.getPartnerTransactionId());
        Assertions.assertTrue(SCT_ORDER_CREATED_STATUS__CREATED.equalsIgnoreCase(refundResponseSuccess.getStatusCode()));

    }

}