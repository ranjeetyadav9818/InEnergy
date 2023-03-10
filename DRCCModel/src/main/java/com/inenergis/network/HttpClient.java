package com.inenergis.network;

import com.inenergis.network.pgerestclient.GsonHelper;
import com.inenergis.network.pgerestclient.model.RequestModel;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
abstract public class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    protected Properties properties;

    protected static final int READ_TIMEOUT_MILLISECONDS = 1000;

    protected abstract Interceptor getInterceptor();

    protected abstract KeyManager[] getKeyManagers() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException;

    private OkHttpClient getClient() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(getKeyManagers(), new TrustManager[]{trustManager}, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        return new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(getInterceptor())
                .followSslRedirects(true)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public <T> T doPost(RequestModel requestModel, Class<T> responseModelClass) throws Exception {
        return doPost(requestModel,responseModelClass,false);
    }


    public <T> T doPost(RequestModel requestModel, Class<T> responseModelClass, boolean parseErrors) throws Exception {
        String url = properties.getProperty(requestModel.getUrlToken());

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, requestModel.toJson()))
                .build();

        log.debug("POST to {}, {}", url, requestModel.toJson());

        Response response = getClient().newCall(request).execute();

        String responseBody = response.body().string();

        log.info("Post message sent to external party. Request: {} response: {}",requestModel.toJson(),responseBody);

        if (!parseErrors && !response.isSuccessful()) {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }

        log.debug("Request took {} ms", response.receivedResponseAtMillis() - response.sentRequestAtMillis());

        return GsonHelper.getGson().fromJson(responseBody, responseModelClass);
    }

    public String doGet(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        log.debug("GET to {}", url);

        Response response = getClient().newCall(request).execute();

        if (!response.isSuccessful()) {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }

        log.debug("Request took {} ms", response.receivedResponseAtMillis() - response.sentRequestAtMillis());

        String string = response.body().string();
        return string;
    }
}