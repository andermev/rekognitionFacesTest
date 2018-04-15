package com.rekognition.andermev.interfaces;

import com.rekognition.andermev.dto.ImageDTO;

import java.io.IOException;

public interface DrawEmoji {
    void drawEmojiOntoImage(ImageDTO emojis) throws IOException;
}
