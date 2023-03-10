package com.inenergis.microbot.camel.processors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AmazonUploadProcessor implements Processor {
    private String accessKey;
    private String secretKey;
    private String bucket;

    private static final Logger log = LoggerFactory.getLogger(AmazonUploadProcessor.class);

    public AmazonUploadProcessor(String accessKey, String secretKey, String bucket) {
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
        final GenericFile body = (GenericFile) exchange.getIn().getBody();
        String fileName = body.getFileNameOnly();
        s3Client.putObject(new PutObjectRequest(bucket, fileName, (File) body.getFile()));
        log.info("File "+fileName+" uploaded to S3 in "+bucket);
    }
}
