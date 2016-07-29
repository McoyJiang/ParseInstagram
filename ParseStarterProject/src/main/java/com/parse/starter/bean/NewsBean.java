package com.parse.starter.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by axing on 16/7/27.
 */
public class NewsBean {

    private String userName;
    private String text;
    private String address;
    private Date createdDate;
    private List<String> allImages = new ArrayList<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void addImage(String imageUrl) {
        allImages.add(imageUrl);
    }

    public List<String> getAllImages() {
        return allImages;
    }

}
