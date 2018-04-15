package com.rekognition.andermev.dto;

import com.amazonaws.services.rekognition.model.BoundingBox;

public class EmojiImageDTO {

    BoundingBox boundingBox;
    String emojiPath;
    String emojiType;

    public EmojiImageDTO(BoundingBox boundingBox, String emojiPath, String emojiType) {
        this.boundingBox = boundingBox;
        this.emojiPath = emojiPath;
        this.emojiType = emojiType;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getEmojiPath() {
        return emojiPath;
    }

    public void setEmojiPath(String emojiPath) {
        this.emojiPath = emojiPath;
    }

    public String getEmojiType() {
        return emojiType;
    }

    public void setEmojiType(String emojiType) {
        this.emojiType = emojiType;
    }
}
