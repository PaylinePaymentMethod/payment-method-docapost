package com.payline.payment.docapost.bean.rest.common;

import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.BuyerPaymentId;
import com.payline.pmapi.bean.payment.response.buyerpaymentidentifier.BuyerPaymentIdVisitor;


/*Classe bouchon cree pour pouvoir aller au bout des test d'integration:
    *todo trouver son utilite
    * todo trouver un nom de classe plus parlant et implementer
    *
 *
 */
public class BuyerPaymentIdImpl implements BuyerPaymentId {
    @Override
    public void accept(BuyerPaymentIdVisitor buyerPaymentIdVisitor) {

// fixme ?!?
    }

}
