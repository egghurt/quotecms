package com.atk.module.web.cms.form;

import java.util.List;

public class QuoteWrapper {
    private List<Quote> quotes;

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public QuoteWrapper() {
        super();
    }

    public QuoteWrapper(List<Quote> quotes) {
        super();
        this.quotes = quotes;
    }
}
