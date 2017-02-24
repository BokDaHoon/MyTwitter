package com.boostcamp.mytwitter.mytwitter.scrap.model;

import java.util.Date;

/**
 * Created by DaHoon on 2017-02-23.
 */

public class SearchDTO {
    private String content;
    private Date startDate;
    private Date endDate;

    public String getContent() {
        return content;
    }

    public SearchDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public SearchDTO setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SearchDTO setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public String toString() {
        return content + " / " + startDate.toString() + " / " + endDate.toString();
    }
}
