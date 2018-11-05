package com.payline.payment.docapost.bean.rest.common;

import com.google.gson.Gson;


public abstract class DocapostBean {


    @Override
    public String toString() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

}