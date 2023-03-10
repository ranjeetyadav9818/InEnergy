package com.inenergis.network.pgerestclient;

import com.inenergis.network.HttpClient;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class PgeHttpClient extends HttpClient {

    private static final int RETRY_COUNT = 3;
    private static final int RETRY_DELAY_MILLISECONDS = 3;

    PgeHttpClient(Properties properties) throws IOException {
        this.properties = properties;
    }

    protected Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = null;
                boolean responseOk = false;
                int tryCount = 1;

                while (!responseOk && tryCount <= RETRY_COUNT) {
                    try {
                        String credentials = Credentials.basic(properties.getProperty("pge.api.username"), properties.getProperty("pge.api.password"));
                        request = request.newBuilder().header("Authorization", credentials).build();
                        response = chain.proceed(request);
                        responseOk = response.isSuccessful();
                    } catch (IOException e) {
                        log.warn("Request #{} {} is not successful: {}", tryCount, request.toString(), e.getMessage());
                        if (tryCount >= RETRY_COUNT) {
                            throw e;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MILLISECONDS);
                        } catch (InterruptedException ie) {
                            log.error(ie.getMessage());
                        }
                    } finally {
                        tryCount++;
                    }
                }

                return response;
            }
        };
    }

    protected KeyManager[] getKeyManagers() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(readKeyStore(), properties.getProperty("pge.certificate.password").toCharArray());

        return keyManagerFactory.getKeyManagers();
    }

    private KeyStore readKeyStore() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks;

        FileInputStream is = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            is = new FileInputStream(properties.getProperty("pge.certificate.location"));// It is not running through as an input stream retry with fileinputstream to check if that's the problem if that's the case investigate but at the end maybe we just can put it somewhere in the server and indicate location through properties
            ks.load(is, properties.getProperty("pge.certificate.password").toCharArray());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ks;
    }
}