package com.egrocery.DO;

/**
 * Created by akolesnik on 7/4/14.
 */
public class ImageDO {
    private String imageId;
    private String imageUrl;

    public ImageDO(String imageId, String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
