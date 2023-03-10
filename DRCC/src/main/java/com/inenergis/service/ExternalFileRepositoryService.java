package com.inenergis.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.inenergis.util.PropertyAccessor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.InputStream;

@Stateless
public class ExternalFileRepositoryService {

    @Inject
    PropertyAccessor propertyAccessor;

    public void uploadFile(String uuid, InputStream inputStream, long contentLength) {
        AmazonS3 s3Client = getAmazonS3Client();
        final String bucket = getBucket();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        s3Client.putObject(bucket, uuid, inputStream, metadata);
    }

    public InputStream getFile(String uuid) {
        AmazonS3 s3Client = getAmazonS3Client();
        final String bucket = getBucket();
        GetObjectRequest objetRequest = new GetObjectRequest(bucket,uuid);
        final S3Object file = s3Client.getObject(objetRequest);
        return file.getObjectContent();
    }

    public void deleteFile(String uuid) {
        AmazonS3 s3Client = getAmazonS3Client();
        final String bucket = getBucket();
        DeleteObjectRequest deleteObjectRequest =new DeleteObjectRequest(bucket,uuid);
        s3Client.deleteObject(deleteObjectRequest);
    }

    private String getBucket() {
        return propertyAccessor.getValue("aws.documents.bucket");
    }

    private AmazonS3 getAmazonS3Client() {
        final String accessKey = propertyAccessor.getValue("aws.documents.accesskey");
        final String secretKey = propertyAccessor.getValue("aws.documents.secretkey");
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AmazonS3Client(credentials);
    }
}