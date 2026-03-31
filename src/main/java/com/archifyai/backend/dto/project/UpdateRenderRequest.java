package com.archifyai.backend.dto.project;

import jakarta.validation.constraints.NotBlank;

public class UpdateRenderRequest {

    @NotBlank
    private String renderedImage;

    private String renderedPath;

    private String thumbnail;

    public String getRenderedImage() {
        return renderedImage;
    }

    public void setRenderedImage(String renderedImage) {
        this.renderedImage = renderedImage;
    }

    public String getRenderedPath() {
        return renderedPath;
    }

    public void setRenderedPath(String renderedPath) {
        this.renderedPath = renderedPath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

