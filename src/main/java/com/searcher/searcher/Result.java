package com.searcher.searcher;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Result {
    private String purchaseObject;
    private String number;
    private String customer;
    private String link;
    public Result(String purchaseObject, String number, String customer, String link) {
        this.purchaseObject = purchaseObject;
        this.number = number;
        this.customer = customer;
        this.link = link;
    }
    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(link, result.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
