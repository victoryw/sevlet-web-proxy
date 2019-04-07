package com.victoryw.picc;

public abstract class User implements java.io.Serializable {

    private String languageId = null;

    public String getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public abstract long getUserId();

    public abstract long getUserType();

    public abstract String getUserName();

    public abstract String getRealName();

    public abstract String getOrganId();

}
