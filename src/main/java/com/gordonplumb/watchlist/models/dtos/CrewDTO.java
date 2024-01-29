package com.gordonplumb.watchlist.models.dtos;

public class CrewDTO extends Credit{
    private String job;

    public CrewDTO() {
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
