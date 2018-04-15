package com.rekognition.andermev.detect;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.rekognition.model.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rekognition.andermev.dto.EmojiImageDTO;
import com.rekognition.andermev.dto.ImageDTO;
import com.rekognition.andermev.interfaces.DrawEmoji;
import com.rekognition.andermev.util.Utils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DetectFacesExample implements DrawEmoji{

    private static final String HAPPY_EMOJI = "emoji/Happy_Emoji.png";
    private static final String SAD_EMOJI = "emoji/Sad_Emoji.png";
    private static final String ANGRY_EMOJI = "emoji/Angry_Emoji.png";
    private static final String CONFUSED_EMOJI = "emoji/Confused_Emoji.png";
    private static final String DISGUSTED_EMOJI = "emoji/Disgusted_Emoji.png";
    private static final String SURPRISED_EMOJI = "emoji/Surprised_Emoji.png";
    private static final String CALM_EMOJI = "emoji/Calm_Emoji.png";
    private static final String UNKNOWN_EMOJI = "emoji/Unknown_Emoji.png";

    private static final String FILE_FORMAT_JPG = ".jpg";
    private static final String MULTIPLE_EMOTIONS = "multiple-emotions";
    private static final String ROOT_EMOJI_PATH = "src/main/resources/emoji-reactions/";

    private static AmazonRekognition rekognitionClient;
    private ByteBuffer imageBytes;

    private ClassLoader classLoader;

    public DetectFacesExample(AmazonRekognition rekognit, String photo){
        rekognitionClient = rekognit;
        imageBytes = Utils.readFile(photo);
    }

    public DetectFacesExample() {
    }

    public List<FaceDetail> detectFacesRequest(){
        DetectFacesRequest request2 = new DetectFacesRequest()
                .withImage(new Image().withBytes(imageBytes))
                .withAttributes(Attribute.ALL);
        // Replace Attribute.ALL with Attribute.DEFAULT to get default values.

        try {
            DetectFacesResult result = rekognitionClient.detectFaces(request2);
            List<FaceDetail> faceDetails = result.getFaceDetails();

            for (FaceDetail face: faceDetails) {
                if (request2.getAttributes().contains("ALL")) {
                    AgeRange ageRange = face.getAgeRange();
                    System.out.println("The detected face is estimated to be between "
                            + ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
                            + " years old.");
                    System.out.println("Here's the complete set of attributes:");
                } else { // non-default attributes have null values.
                    System.out.println("Here's the default set of attributes:");
                }

                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
            }

            return faceDetails;

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void drawImage(List<FaceDetail> faces) throws IOException {

        ArrayList<EmojiImageDTO> emojis = new ArrayList();

        if(faces.size() > 0){
            for(FaceDetail face : faces){

                float max = 0;
                String emojiType = "";
                String emojiPath = "";

                for(Emotion emotion : face.getEmotions()){
                    if(emotion.getConfidence() > max){
                        max = emotion.getConfidence();
                        emojiType = emotion.getType();
                    }
                }

                emojiPath = getEmotionEmoji(emojiType);

                EmojiImageDTO emojiImageDTO = new EmojiImageDTO(face.getBoundingBox(), emojiPath, emojiType);
                emojis.add(emojiImageDTO);
            }

            ImageDTO images = new ImageDTO(imageBytes, emojis);

            drawEmojiOntoImage(images);
        } else {
            System.out.println("Faces not found");
        }
    }

    @Override
    public void drawEmojiOntoImage(ImageDTO images) throws IOException {

        Graphics2D paint;

        //Get image width and height
        BufferedImage image = Utils.getImage(images.getImageBytes());

        paint = image.createGraphics();

        classLoader = new DetectFacesExample().getClass().getClassLoader();

        for(EmojiImageDTO emoji : images.getEmojis()){
            int height = image.getHeight();
            int width = image.getWidth();
            float left = width * emoji.getBoundingBox().getLeft();
            float top = height * emoji.getBoundingBox().getTop();
            float heightEmoji = height * emoji.getBoundingBox().getHeight();
            float widthEmoji = width * emoji.getBoundingBox().getWidth();

            try{
                File file = new File(classLoader.getResource(emoji.getEmojiPath()).getFile());
                java.awt.Image imageEmoji = ImageIO.read(file)
                        .getScaledInstance((int)widthEmoji, (int)heightEmoji, 1);
                paint.drawImage(imageEmoji, (int)left, (int)top, null);
            }catch (IIOException exc){
                exc.printStackTrace();
            }
        }

        paint.dispose();

        //Create output
        if(images.getEmojis().size() > 0){
            String emotionType;
            if(images.getEmojis().size() == 1){
                emotionType = images.getEmojis().get(0).getEmojiType();
            }else{
                emotionType = MULTIPLE_EMOTIONS;
            }

            createEmojiImage(emotionType, image);
        }
    }

    private String getEmotionEmoji(String emojiType) {

        String emojiPath;

        if (EmotionName.HAPPY.toString().equals(emojiType)) {
            emojiPath = HAPPY_EMOJI;
        }else if (EmotionName.SAD.toString().equals(emojiType)) {
            emojiPath = SAD_EMOJI;
        }else if (EmotionName.ANGRY.toString().equals(emojiType)) {
            emojiPath = ANGRY_EMOJI;
        }else if (EmotionName.CONFUSED.toString().equals(emojiType)) {
            emojiPath = CONFUSED_EMOJI;
        }else if (EmotionName.DISGUSTED.toString().equals(emojiType)) {
            emojiPath = DISGUSTED_EMOJI;
        }else if (EmotionName.SURPRISED.toString().equals(emojiType)) {
            emojiPath = SURPRISED_EMOJI;
        }else if (EmotionName.CALM.toString().equals(emojiType)) {
            emojiPath = CALM_EMOJI;
        }else if (EmotionName.UNKNOWN.toString().equals(emojiType)) {
            emojiPath = UNKNOWN_EMOJI;
        }else{
            emojiPath = UNKNOWN_EMOJI;
        }
        return emojiPath;
    }

    private void createEmojiImage(String emotionType, BufferedImage image) throws IOException {

        StringBuilder emojiPhoto = new StringBuilder(ROOT_EMOJI_PATH);

        emojiPhoto.append("emoji-");
        emojiPhoto.append(emotionType.toLowerCase());
        emojiPhoto.append("-");
        emojiPhoto.append(System.currentTimeMillis());
        emojiPhoto.append(FILE_FORMAT_JPG);

        OutputStream outfile = new FileOutputStream(emojiPhoto.toString());
        JPEGImageEncoder encoder2 = JPEGCodec.createJPEGEncoder(outfile);
        encoder2.encode(image);
        outfile.close();
    }

}
