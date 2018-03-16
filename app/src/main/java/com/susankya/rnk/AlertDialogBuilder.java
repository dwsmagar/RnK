package com.susankya.rnk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ajay on 9/4/2015.
 */
public class AlertDialogBuilder {
    private String title,message,positiveText,negativeText;
    private Context activity;

    public AlertDialogBuilder(String titleOfDialog,String messageOfDialog,String positiveButtonText,String negativeButtonText,Context act)
    {
        title=titleOfDialog;
        message=messageOfDialog;
        positiveText=positiveButtonText;
        negativeText=negativeButtonText;
        activity=act;
        builderBoy();
    }

    private void builderBoy()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        if(Utilities.isValidString(title))
        builder1.setTitle(title);
        if (Utilities.isValidString(message))
        builder1.setMessage(message);

        builder1.setCancelable(true);
        if(Utilities.isValidString(positiveText))
        {
            builder1.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        if (Utilities.isValidString(negativeText))
        {
            builder1.setNegativeButton(
                    negativeText,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }

            );
        }


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
