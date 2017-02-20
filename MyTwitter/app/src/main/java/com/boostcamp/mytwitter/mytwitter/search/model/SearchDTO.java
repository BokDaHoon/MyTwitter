package com.boostcamp.mytwitter.mytwitter.search.model;

/**
 * Created by DaHoon on 2017-02-17.
 */

public class SearchDTO {
    private int rank;
    private String searchWord;

    public int getRank() {
        return rank;
    }

    public SearchDTO setRank(int rank) {
        this.rank = rank;
        return this;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public SearchDTO setSearchWord(String searchWord) {
        this.searchWord = searchWord;
        return this;
    }
}
