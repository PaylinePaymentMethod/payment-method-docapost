package com.payline.payment.docapost.Integration;

import com.payline.payment.docapost.utils.type.WSRequestResultEnum;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.refund.request.RefundRequest;
import com.payline.pmapi.bean.refund.response.RefundResponse;
import com.payline.pmapi.bean.refund.response.impl.RefundResponseFailure;
import org.junit.jupiter.api.Assertions;

public class TestItRefundCaseFailure extends AbstractTestItRefundCase {

    public static void main(String[] args) {

        // To get a refund action with success error as EXISTING_SCTORDER, a not unique transactionId must be used

        main();

        //**************************************************************************************************************
        // SCT ORDER CREATE for refund

        // Refund Request :
        // => API MandateWS /api/sctorder/create

        // Create the RefundRequest, execute the request and get the response result
        RefundRequest refundRequest = createRefundRequest(REFUND_ADDITIONAL_DATA, REFUND_PARTNER_TRANSACTION_ID);
        RefundResponse refundResponseFromRefundRequest = TEST_IT.refundService.refundRequest(refundRequest);

        // ... check the response (should be a RefundResponseFailure)
        checkRefundResponse(refundResponseFromRefundRequest);

    }

    /**
     * Check the refund request response
     *
     * @param refundResponse
     */
    private static void checkRefundResponse(RefundResponse refundResponse) {

        TEST_IT.checkRefundResponseIsFailure(refundResponse);
        TEST_IT.checkRefundResponseIsRightClass("refundRequest", refundResponse, RefundResponseFailure.class);

        RefundResponseFailure refundResponseFailure = (RefundResponseFailure) refundResponse;

        Assertions.assertEquals(WSRequestResultEnum.EXISTING_SCT_ORDER.getDocapostErrorCode(), refundResponseFailure.getErrorCode());
        Assertions.assertEquals(FailureCause.INVALID_DATA, refundResponseFailure.getFailureCause());

    }

}