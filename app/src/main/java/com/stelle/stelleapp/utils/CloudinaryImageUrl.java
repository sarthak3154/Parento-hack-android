package com.stelle.stelleapp.utils;

import android.text.TextUtils;

public class CloudinaryImageUrl {

    public static final int CROP_NONE = -1;
    public static final int CROP_FILL = 0; //c_fill
    public static final int CROP_THUMB = 1; //c_thumb

    public static final String CROP_FILL_VALUE = "c_fill";
    public static final String CROP_THUMB_VALUE = "c_thumb";


    public static final int GRAVITY_FACES_NONE = -1;
    public static final int GRAVITY_FACES_CENTER = 0; //g_faces:center
    public static final String GRAVITY_FACES_CENTER_VALUE = "g_faces:center"; //g_faces:center


    public static final int DEFAULT_CORNER_RADIUS = 0;

    public static final int DEFAULT_EXTENSION = 0;

    private static final String EXTENSION_STRING_JPG = ".jpg";

    private final String baseUrl;
    private final String imageName;
    private final int width;
    private final int height;
    private final String baseURLPostFix;
    private final int cornerRadius;
    private final int crop;
    private final int gravity;
    private final int extension;

    public static class Builder {

        //Required parameters
        private final String baseURL;
        private final String imageName;
        private final int width;
        private final int height;
        private final String baseURLPostFix;

        // Optional parameters - initialized to default values
        private int cornerRadius = DEFAULT_CORNER_RADIUS;
        private int crop = CROP_FILL;
        private int gravity = GRAVITY_FACES_CENTER;
        private int extension = DEFAULT_EXTENSION;

        public Builder(String baseURL, String imageName, int width, int height, String baseURLPostFix) {
            this.baseURL = baseURL;
            this.imageName = imageName;
            this.height = height;
            this.width = width;
            this.baseURLPostFix = baseURLPostFix;
        }

        public Builder cornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public Builder crop(int crop) {
            this.crop = crop;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder extension(int extension) {
            this.extension = extension;
            return this;
        }

        public CloudinaryImageUrl build() {
            return new CloudinaryImageUrl(this);
        }
    }

    private CloudinaryImageUrl(Builder builder) {
        this.baseUrl = builder.baseURL;
        this.imageName = builder.imageName;
        this.height = builder.height;
        this.width = builder.width;
        this.baseURLPostFix = builder.baseURLPostFix;
        this.cornerRadius = builder.cornerRadius;
        this.crop = builder.crop;
        this.gravity = builder.gravity;
        this.extension = builder.extension;

    }

    public String getTransformedImageUrl() {
        String imageUrl = baseUrl;

        if (baseUrl == null || !baseUrl.endsWith("/")) {
            //throw new RuntimeException("Cloudinary base url should end with /");
            return null;
        }

        if (TextUtils.isEmpty(imageName)) {
            //throw new RuntimeException("Cloudinary image name should not be null or empty");
            return null;
        }

        imageUrl += "w_" + width + ",";
        imageUrl += "h_" + height + ",";

        if (cornerRadius != 0) {
            imageUrl += "r_" + cornerRadius + ",";
        }

        //Previously commented
        imageUrl += getCropValue(crop) + ",";
        imageUrl += getGravityValue(gravity) + ",";


        if (imageUrl.endsWith(",")) {
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(",")); //remove trailing comma
        }
        imageUrl += "/" + baseURLPostFix;
        String extensionStr = getExtensionString(extension);
        imageUrl += "/" + imageName + extensionStr;
        return imageUrl;
    }

    private String getExtensionString(int extension) {
        switch (extension) {
            case DEFAULT_EXTENSION:
            default:
                return EXTENSION_STRING_JPG;
        }
    }

    private String getCropValue(int crop) {
        switch (crop) {
            case CROP_FILL:
                return CROP_FILL_VALUE;
            case CROP_THUMB:
                return CROP_THUMB_VALUE;
            case CROP_NONE:
            default:
                return "";
        }
    }

    private String getGravityValue(int gravity) {
        switch (gravity) {
            case GRAVITY_FACES_CENTER:
                return GRAVITY_FACES_CENTER_VALUE;
            case GRAVITY_FACES_NONE:
            default:
                return "";
        }
    }

}
