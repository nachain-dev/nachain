package org.nachain.core.token.nft.collection;


public class NftCollectionDetail {


    private String coverIcon;


    private String coverPicture;


    private String info;

    private String websiteUrl;
    private String twitterUrl;
    private String telegramUrl;
    private String facebookUrl;
    private String emailUrl;
    private String mediumUrl;
    private String linkinUrl;
    private String instagramUrl;

    public NftCollectionDetail() {
    }

    public String getCoverIcon() {
        return coverIcon;
    }

    public void setCoverIcon(String coverIcon) {
        this.coverIcon = coverIcon;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getEmailUrl() {
        return emailUrl;
    }

    public void setEmailUrl(String emailUrl) {
        this.emailUrl = emailUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getLinkinUrl() {
        return linkinUrl;
    }

    public void setLinkinUrl(String linkinUrl) {
        this.linkinUrl = linkinUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    @Override
    public String toString() {
        return "NftCollectionDetail{" +
                "coverIcon='" + coverIcon + '\'' +
                ", coverPicture='" + coverPicture + '\'' +
                ", info='" + info + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", telegramUrl='" + telegramUrl + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                ", emailUrl='" + emailUrl + '\'' +
                ", mediumUrl='" + mediumUrl + '\'' +
                ", linkinUrl='" + linkinUrl + '\'' +
                ", instagramUrl='" + instagramUrl + '\'' +
                '}';
    }
}
