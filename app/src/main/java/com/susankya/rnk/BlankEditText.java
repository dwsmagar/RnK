package com.susankya.rnk;

import android.widget.EditText;

/**
 * Created by ajay on 9/4/2015.
 */
public class BlankEditText {


    private String[] errorsToDisplay = null;
    private EditText[] editTexts;


    public BlankEditText(String[] error, EditText[] etArray) {
        errorsToDisplay = error;
        editTexts = etArray;
    }

    public boolean areAllFieldsOK() {
        boolean result = true;
        for (int i = 0; i < editTexts.length; i++) {
            String textOfField = editTexts[i].getText().toString().trim();
            if (!Utilities.isValidString(textOfField)) {
                editTexts[i].setError(errorsToDisplay[i]);
                result = false;
            }
        }
        return result;
    }
}
