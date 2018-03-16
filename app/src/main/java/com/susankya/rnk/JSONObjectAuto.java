package com.susankya.rnk;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajay on 12/27/2015.
 */
public class JSONObjectAuto extends JSONObject {

    @Override
    public int getInt(String name) throws JSONException {

        if(!this.isNull(name))
            return super.getInt(name);
        else return 0;
    }

    public JSONObjectAuto(JSONObject jsonObject) throws JSONException
    {
        super(jsonObject.toString());
    }
    @Override
    public String getString(String name) throws JSONException {
        if(!this.isNull(name))
            return super.getString(name);
        else return "";
    }
}
