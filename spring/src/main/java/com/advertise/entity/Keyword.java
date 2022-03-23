package com.advertise.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="keyword")
public class Keyword {

    @Id
    @Column(name="keyword_id")
    private String keywordId;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "max_cpc")
    private Double maxCPC;  //--> Click

    @Column(name = "max_cpm")
    private Double maxCPM;  //--> Impression ~ Display 1000 times

    @Column(name = "max_cpv")
    private Double maxCPV;  //--> View

    @Column(name = "match_type")
    private String matchType;

}
