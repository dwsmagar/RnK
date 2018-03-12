package com.susankya.schoolvalley;

/**
 * Created by Aditya on 12/2/2016.
 */
public class AboutInfo {

    String field;
String column_name;

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    String data;
    boolean isBold;

    public AboutInfo(String field, String data)
    {
        this.field=field;
        this.data=data;
    }
}
