package com.rekognition.andermev.util;

import com.amazonaws.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class Utils {

    public static ByteBuffer readFile(String photoPath){

        ByteBuffer imageBytes;

        ClassLoader classLoader = new Utils().getClass().getClassLoader();
        File file = new File(classLoader.getResource(photoPath).getFile());

        try (InputStream inputStream = new FileInputStream(file)) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
            return imageBytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BufferedImage getImage(ByteBuffer imageByte) throws IOException {
        InputStream imageBytesStream;
        imageBytesStream = new ByteArrayInputStream(imageByte.array());
        return ImageIO.read(imageBytesStream);
    }
}
