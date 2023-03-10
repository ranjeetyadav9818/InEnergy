package com.inenergis.controller.audio;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.ByteStreams;
import com.inenergis.util.PropertyAccessor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/audio")
public class AudioController extends HttpServlet {

    /**
     * Private logger for this class
     */
    private static final Logger log = LoggerFactory.getLogger(AudioController.class);

    @Inject
    PropertyAccessor propertyAccessor;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String accessKey = propertyAccessor.getValue("aws.voicefiles.accesskey");
        final String secretKey = propertyAccessor.getValue("aws.voicefiles.secretkey");
        final String bucket = propertyAccessor.getValue("aws.voicefiles.bucket");
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        GetObjectRequest objetRequest = new GetObjectRequest(bucket,request.getParameter("fileName"));
        final S3Object audio = s3Client.getObject(objetRequest);
        response.setContentType("audio/wav");
        ByteStreams.copy(audio.getObjectContent(),response.getOutputStream());
    }

    public void setPropertyAccessor(PropertyAccessor propertyAccessor) {
        this.propertyAccessor = propertyAccessor;
    }
}
