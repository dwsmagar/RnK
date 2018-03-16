package com.susankya.rnk;

public class Sms {
    public int count ;
    public int response_code;
    public String response;
    public int credits_available, credits_consumed;

    public int getCount() {
        return count;
    }

    public int getResponse_code() {
        return response_code;
    }

    public String getResponse() {
        return response;
    }

    public int getCredits_available() {
        return credits_available;
    }

    public int getCredits_consumed() {
        return credits_consumed;
    }
}
