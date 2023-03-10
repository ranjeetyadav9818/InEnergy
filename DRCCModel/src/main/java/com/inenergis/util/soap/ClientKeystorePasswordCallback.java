package com.inenergis.util.soap;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientKeystorePasswordCallback implements CallbackHandler {

    private Map<String, String> passwords =
            new HashMap<String, String>();

    public ClientKeystorePasswordCallback() {
        //put one key-value per alias used
        passwords.put("te-c665f2da-ae39-4759-8e34-d98e258166d3", "F7Y4nw9Egd5t");
        passwords.put("te-ed444cb9-64e5-402b-bfa4-6ad77a6eda48", "F7Y4nw9Egd5t");
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];

            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }
    }
}