package com.inenergis.microbot.camel.processors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.inenergis.microbot.camel.beans.PdpSrNotification;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmazonDeleteProcessor implements Processor {
    private String accessKey;
    private String secretKey;
    private String bucket;

    private static final Logger log = LoggerFactory.getLogger(AmazonDeleteProcessor.class);

    public AmazonDeleteProcessor(String accessKey, String secretKey, String bucket) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        final PdpSrNotification notification = (PdpSrNotification) exchange.getIn().getBody();
        s3Client.deleteObject(new DeleteObjectRequest(bucket,notification.getVoiceFileName()));
        log.info("File "+notification.getVoiceFileName()+" deleted from S3 in "+bucket);
    }
}
