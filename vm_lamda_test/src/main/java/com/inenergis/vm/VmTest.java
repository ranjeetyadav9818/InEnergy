package com.inenergis.vm;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class VmTest implements RequestHandler<String, String> {
    public String handleRequest(String input, Context context) {
        return "Hello, " + input + "! IntelliJ";
    }
}