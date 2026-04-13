package com.example.demo.dto;

public class MyListsDto {

    private String listsId;
    private String companyId;
    private String userId;
    private String listsTitle;
    private String listsText;
    private String showAuth;
    private String createdAt;
    private String updatedAt;
    private String showPublicAuth;

    // ===== getter / setter =====

    public String getListsId() {
        return listsId;
    }

    public void setListsId(String listsId) {
        this.listsId = listsId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getListsTitle() {
        return listsTitle;
    }

    public void setListsTitle(String listsTitle) {
        this.listsTitle = listsTitle;
    }

    public String getListsText() {
        return listsText;
    }

    public void setListsText(String listsText) {
        this.listsText = listsText;
    }

    public String getShowAuth() {
        return showAuth;
    }

    public void setShowAuth(String showAuth) {
        this.showAuth = showAuth;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShowPublicAuth() {
        return showPublicAuth;
    }

    public void setShowPublicAuth(String showPublicAuth) {
        this.showPublicAuth = showPublicAuth;
    }
}