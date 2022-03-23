package com.advertise.dto;

public class KeywordDTO {

    private String keywordId;
    private String keyword;
    private Double maxCPC;  //--> Click
    private Double maxCPM;  //--> Impression ~ Display 1000 times
    private Double maxCPV;  //--> View
    private String matchType;

    public KeywordDTO(String keywordId, String keyword, Double maxCPC, Double maxCPM, Double maxCPV, String matchType) {
        this.keywordId = keywordId;
        this.keyword = keyword;
        this.maxCPC = maxCPC;
        this.maxCPM = maxCPM;
        this.maxCPV = maxCPV;
        this.matchType = matchType;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public String getKeyword() {
        return keyword;
    }

    public Double getMaxCPC() {
        return maxCPC;
    }

    public Double getMaxCPM() {
        return maxCPM;
    }

    public Double getMaxCPV() {
        return maxCPV;
    }

    public String getMatchType() {
        return matchType;
    }
}
