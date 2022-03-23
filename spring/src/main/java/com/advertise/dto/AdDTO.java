package com.advertise.dto;

public class AdDTO {
    private Integer adID;
    private String adName;
    private String adStatus;
    private String adType;
    private Integer bigModifier;

    public AdDTO(Integer adID, String adName, String adStatus, String adType, Integer bigModifier) {
        this.adID = adID;
        this.adName = adName;
        this.adStatus = adStatus;
        this.adType = adType;
        this.bigModifier = bigModifier;
    }

    public Integer getAdID() {
        return adID;
    }

    public String getAdName() {
        return adName;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public String getAdType() {
        return adType;
    }

    public Integer getBigModifier() {
        return bigModifier;
    }
}
