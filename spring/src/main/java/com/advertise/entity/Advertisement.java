package com.advertise.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.io.Serializable;

@Entity
@Table(name = "advertisement")
public class Advertisement implements Serializable {
    public static final int adID_ColumnIndex = 0;
    public static final int adName_ColumnIndex = 1;
    public static final int adStatus_ColumnIndex = 2;
    public static final int adType_ColumnIndex = 3;
    public static final int bigModifier_ColumnIndex = 4;

    @Id
    @Column(name = "ad_id")
    private Integer adID;

    @Column(name = "ad_name")
    private String adName;

    @Column(name = "ad_status")
    private String adStatus;

    @Column(name = "ad_type")
    private String adType;

    @Column(name = "big_modifier")
    private Integer bigModifier;

    public Advertisement(Integer adID, String adName, String adStatus, String adType, Integer bigModifier) {
        this.adID = adID;
        this.adName = adName;
        this.adStatus = adStatus;
        this.adType = adType;
        this.bigModifier = bigModifier;
    }

    public Advertisement(){}

    public Integer getAdID() {
        return adID;
    }

    public void setAdID(Integer adID) {
        this.adID = adID;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public Integer getBigModifier() {
        return bigModifier;
    }

    public void setBigModifier(Integer bigModifier) {
        this.bigModifier = bigModifier;
    }

}
