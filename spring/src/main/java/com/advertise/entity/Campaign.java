package com.advertise.entity;

import org.springframework.data.relational.core.mapping.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "Campaign")
public class Campaign {
    public static final int campaignID_ColumnIndex = 0;
    public static final int campaignName_ColumnIndex = 1;
    public static final int campaignStatus_ColumnIndex = 2;
    public static final int startDate_ColumnIndex = 3;
    public static final int endDate_ColumnIndex = 4;
    public static final int budget_ColumnIndex = 5;

    @Id
    @Column
    private Integer campaignID;
    @Column
    private String campaignName;
    @Column
    private String campaignStatus;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private Integer budget;

    public Campaign(Integer campaignID, String campaignName, String campaignStatus, Date startDate, Date endDate, Integer budget) {
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.campaignStatus = campaignStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public Campaign() {
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(Integer campaignID) {
        this.campaignID = campaignID;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

}
