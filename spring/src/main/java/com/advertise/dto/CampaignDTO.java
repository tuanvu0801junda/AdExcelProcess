package com.advertise.dto;

import javax.persistence.Column;
import java.sql.Date;

public class CampaignDTO {

    private Integer campaignID;
    private String campaignName;
    private String campaignStatus;
    private Date startDate;
    private Date endDate;
    private Integer budget;

    public CampaignDTO(Integer campaignID, String campaignName, String campaignStatus, Date startDate, Date endDate, Integer budget) {
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.campaignStatus = campaignStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getCampaignStatus() {
        return campaignStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getBudget() {
        return budget;
    }
}
