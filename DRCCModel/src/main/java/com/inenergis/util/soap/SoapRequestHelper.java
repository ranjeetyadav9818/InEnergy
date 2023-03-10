package com.inenergis.util.soap;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;


public final class SoapRequestHelper {

    public static String zipAndBase64AString(String string) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = new GZIPOutputStream(baos);
        gzos.write(string.getBytes("UTF-8"));
        gzos.close();
        byte[] zippedLocationData = baos.toByteArray();
        String encBytes = new String(Base64.getEncoder().encode(zippedLocationData));
        return encBytes;
    }

    public static void addSignatureToHeader(Client client, String signatureParts, String user, String storePropertiesLocation) {

        Map<String, Object> outProps = new HashMap<>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
        outProps.put(WSHandlerConstants.SIGNATURE_PARTS, signatureParts);
        outProps.put(WSHandlerConstants.USER, user);
        outProps.put(WSHandlerConstants.SIG_ALGO, "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
        outProps.put(WSHandlerConstants.SIG_PROP_FILE, storePropertiesLocation);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, "com.inenergis.util.soap.ClientKeystorePasswordCallback");
        outProps.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");


        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        client.getOutInterceptors().add(wssOut);
// Add SOAP headers to the web service request
    }

    public static void addLoggingToCommunication(Client client, boolean inbound, boolean outbound) {
        if (inbound) {
            client.getInInterceptors().add(new LoggingInInterceptor());
        }
        if (outbound) {
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }
    }

    public static void addSSL(Client client, String storeLoc, String storePassword, String storeType) throws Exception {

        final TLSClientParameters tlsCP = new TLSClientParameters();

        final KeyStore keyStore = KeyStore.getInstance(storeType);
        keyStore.load(SoapRequestHelper.class.getClassLoader().getResourceAsStream(storeLoc), storePassword.toCharArray());
        final KeyManager[] myKeyManagers = getKeyManagers(keyStore, storePassword);
        tlsCP.setKeyManagers(myKeyManagers);

        final KeyStore trustStore = KeyStore.getInstance(storeType);
        trustStore.load(SoapRequestHelper.class.getClassLoader().getResourceAsStream(storeLoc), storePassword.toCharArray());
        final TrustManager[] myTrustStoreKeyManagers = getTrustManagers();
        tlsCP.setTrustManagers(myTrustStoreKeyManagers);

        ((HTTPConduit) client.getConduit()).setTlsClientParameters(tlsCP);
    }

    private static TrustManager[] getTrustManagers() {
        TrustManager trm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {

            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        };
        return new TrustManager[]{trm};
    }

    private static KeyManager[] getKeyManagers(KeyStore keyStore, String keyPassword) throws GeneralSecurityException, IOException {
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        char[] keyPass = keyPassword != null ? keyPassword.toCharArray() : null;
        KeyManagerFactory fac = KeyManagerFactory.getInstance(alg);
        fac.init(keyStore, keyPass);
        return fac.getKeyManagers();
    }

    public static void disableTLSValidation(Client client) {
        final TLSClientParameters tlsCP = new TLSClientParameters();
        tlsCP.setTrustManagers(getTrustManagers());

        ((HTTPConduit) client.getConduit()).setTlsClientParameters(tlsCP);
    }
}
