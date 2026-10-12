package com.example.demo.dto;

public class ProductImageDto {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProductImageDto(String url) {
        super();
        this.url = url;
    }

    private String url;

}
