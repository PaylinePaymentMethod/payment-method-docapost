package com.payline.payment.docapost.bean.rest.request.mandate;

import com.payline.payment.docapost.TestUtils;
import com.payline.payment.docapost.exception.InvalidRequestException;
import com.payline.payment.docapost.utils.DocapostConstants;
import com.payline.pmapi.bean.common.Amount;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Currency;

import static com.payline.payment.docapost.utils.DocapostConstants.*;

/**
 * SddOrderCreateRequest Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>nov. 6, 2018</pre>
 */
public class SddOrderCreateRequestTest {

    private SddOrderCreateRequest sddOrderCreateRequest;

//    private Gson gson;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Method: getCreditorId()
     */
    @Before
    public void setUp() {
        sddOrderCreateRequest = new SddOrderCreateRequest("creditorId",
                "rum",
                1.1f,
                "label",
                "e2eId");
//        gson = new Gson();
    }

    /**
     * Method: getCreditorId()
     */
    @Test
    public void getCreditorId() {
        String creditorId = sddOrderCreateRequest.getCreditorId();
        Assert.assertEquals("creditorId", creditorId);
    }


    /**
     * Method: getRum()
     */
    @Test
    public void testGetRum() {
        String rum = sddOrderCreateRequest.getRum();
        Assert.assertEquals("rum", rum);
    }

    /**
     * Method: getAmount()
     */
    @Test
    public void testGetAmount() {
        float amount = sddOrderCreateRequest.getAmount();
        Assert.assertEquals(1.1f, amount, 0);
    }

    /**
     * Method: getLabel()
     */
    @Test
    public void testGetLabel() {
        String label = sddOrderCreateRequest.getLabel();
        Assert.assertEquals("label", label);
    }

    /**
     * Method: getE2eId()
     */
    @Test
    public void testGetE2eId() {
        String e2eId = sddOrderCreateRequest.getE2eId();
        Assert.assertEquals("e2eId", e2eId);
    }

    /**
     * Method: toString()
     */
    @Test
    public void testToString() {

        String result = sddOrderCreateRequest.toString();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("creditorId"));
    }

    /**
     * Method: fromPaylineRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testFromPaylineRequest() throws Exception {
        final ContractConfiguration contractConfiguration = TestUtils.createContractConfiguration();
        final Amount amount = TestUtils.createAmount("EUR");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
        Assert.assertNotNull(paylineRequest);
        Assert.assertEquals(paylineRequest.getContractConfiguration().getContractProperties().get(DocapostConstants.CONTRACT_CONFIG_CREDITOR_ID).getValue(),
                sddOrderCreateRequest.getCreditorId());
        Assert.assertEquals("rum", sddOrderCreateRequest.getRum());
        Assert.assertEquals(amount.getAmountInSmallestUnit().floatValue(), sddOrderCreateRequest.getAmount(), 0);
        Assert.assertEquals(paylineRequest.getSoftDescriptor(), sddOrderCreateRequest.getLabel());
        Assert.assertEquals(paylineRequest.getTransactionId(), sddOrderCreateRequest.getE2eId());
//        Assert.assertEquals(
//                gson.toJson(contractConfiguration), gson.toJson(paylineRequest.getContractConfiguration()));
    }


    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_ContractConfiguration_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Contract configuration properties object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();

        FieldUtils.writeField(paylineRequest, "contractConfiguration", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }


    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_null() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Request must not be null");

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(null);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_creditorId_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing contract configuration property: creditor id");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getContractConfiguration().getContractProperties().remove(CONTRACT_CONFIG_CREDITOR_ID);
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_RequestContext_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Request context object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        FieldUtils.writeField(paylineRequest, "requestContext", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_RequestData_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Request context object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest.getRequestContext(), "requestData", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_rum_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing context data: mandate rum");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().remove(CONTEXT_DATA_MANDATE_RUM);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_PartnerConfiguration_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Partner configuration sensitive properties object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest, "partnerConfiguration", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_SensitiveProperties_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Partner configuration sensitive properties object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest.getPartnerConfiguration(), "sensitivePartnerConfigurationMap", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_authLogin_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing contract configuration property: auth login");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        paylineRequest.getContractConfiguration().getContractProperties().remove(PARTNER_CONFIG_AUTH_LOGIN);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }


    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_authPass_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing contract configuration property: auth pass");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        paylineRequest.getContractConfiguration().getContractProperties().remove(PARTNER_CONFIG_AUTH_PASS);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_transactionId_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing mandatory property: transaction id");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest, "transactionId", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_SoftDescriptor_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing mandatory property: soft descriptor");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest, "softDescriptor", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_Order_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Order object must not be null");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest, "order", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_Amount_ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Missing mandatory property: amount");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest, "amount", null, true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }

    /**
     * Method: checkInputRequest(PaymentRequest paylineRequest)
     */
    @Test
    public void testCheckInputRequest_Currency_Ko() throws Exception {
        expectedEx.expect(InvalidRequestException.class);
        expectedEx.expectMessage("Currency must be in euro");
        PaymentRequest paylineRequest = TestUtils.createDefaultPaymentRequest();
        paylineRequest.getRequestContext().getRequestData().put(CONTEXT_DATA_MANDATE_RUM, "rum");
        FieldUtils.writeField(paylineRequest.getAmount(), "currency", Currency.getInstance("BRL"), true);

        SddOrderCreateRequest sddOrderCreateRequest = new SddOrderCreateRequest.Builder().fromPaylineRequest(paylineRequest);
    }


} 
