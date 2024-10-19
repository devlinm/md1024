package com.toolrental.domain.entity;

public class Tool implements Entity<String> {

    private String code;
    private String type;
    private String brand;

    public Tool(String code, String type, String brand){
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

    @Override
    public String getId() {
        return getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
