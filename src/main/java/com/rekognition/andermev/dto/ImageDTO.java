package com.rekognition.andermev.dto;

import java.nio.ByteBuffer;
import java.util.List;

public class ImageDTO {
    private ByteBuffer imageBytes;
    private List<EmojiImageDTO> emojis;

    public ImageDTO(ByteBuffer imageBytes, List<EmojiImageDTO> emojis) {
        this.imageBytes = imageBytes;
        this.emojis = emojis;
    }

    public ByteBuffer getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(ByteBuffer imageBytes) {
        this.imageBytes = imageBytes;
    }

    public List<EmojiImageDTO> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<EmojiImageDTO> emojis) {
        this.emojis = emojis;
    }
}
