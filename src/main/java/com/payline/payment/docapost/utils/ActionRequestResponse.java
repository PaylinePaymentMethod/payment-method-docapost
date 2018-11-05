package com.payline.payment.docapost.utils;

import com.payline.payment.docapost.utils.http.StringResponse;

public enum ActionRequestResponse {
    OK_200, OTHER_CODE, DEFAULT;

    public static ActionRequestResponse checkResponse(StringResponse response) {
        if (response != null && response.getCode() == 200 && response.getContent() != null) {
            return OK_200;
        } else if (response != null && response.getCode() != 200) {
            return OTHER_CODE;
        } else {
            return DEFAULT;
        }
    }
}
