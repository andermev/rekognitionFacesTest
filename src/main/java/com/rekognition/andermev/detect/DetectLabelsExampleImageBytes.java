package com.rekognition.andermev.detect;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.rekognition.andermev.util.Utils;

import java.nio.ByteBuffer;
import java.util.List;

public class DetectLabelsExampleImageBytes {

    private static AmazonRekognition rekognitionClient;
    private String photo;
    private ByteBuffer imageBytes;

    public DetectLabelsExampleImageBytes(AmazonRekognition rekognit, String pho){
        rekognitionClient = rekognit;
        photo = pho;
        imageBytes = Utils.readFile(photo);
    }

    public void detectLabelsRequest(){

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(10)
                .withMinConfidence(77F);

        try {

            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List <Label> labels = result.getLabels();

            System.out.println("Detected labels for " + photo);
            for (Label label: labels) {
                System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
    }
}

