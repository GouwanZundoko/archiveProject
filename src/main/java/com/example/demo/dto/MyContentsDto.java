package com.example.demo.dto;

public class MyContentsDto {

    private String contentsId;
    private String companyId;
    private String userId;
    private String tag;
    private String contentsTitle;
    private String imageUrl;
    private String movieUrl;
    private String fileUrl;
    private String contentsText;
    private String showAuth;
    private String showPublicAuth;
    private String createDate;
    private String updateDate;

    // getter / setter
    public String getContentsId() {
        return contentsId;
    }

    public void setContentsId(String contentsId) {
        this.contentsId = contentsId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContentsTitle() {
        return contentsTitle;
    }

    public void setContentsTitle(String contentsTitle) {
        this.contentsTitle = contentsTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContentsText() {
        return contentsText;
    }

    public void setContentsText(String contentsText) {
        this.contentsText = contentsText;
    }

    public String getShowAuth() {
        return showAuth;
    }

    public void setShowAuth(String showAuth) {
        this.showAuth = showAuth;
    }

    public String getShowPublicAuth() {
        return showPublicAuth;
    }

    public void setShowPublicAuth(String showPublicAuth) {
        this.showPublicAuth = showPublicAuth;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}