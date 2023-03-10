package com.inenergis.egamas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;



public class Test implements RequestHandler<Pojo, String> {

    public String handleRequest(Pojo pojo, Context context) {
        System.out.println("Let s see what happens with sout "+pojo.getMsg());
        return "Connected AWS from intellij "+pojo.getMsg();
    }
}