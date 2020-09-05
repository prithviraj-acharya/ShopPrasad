package com.prithviraj.shopprasad.utils;

import android.content.Context;
import android.content.DialogInterface;

public class ErrorDialog {

    private String message;
    private Context context;

    public ErrorDialog(String message, Context context) {
        this.message = message;
        this.context = context;
    }

   public void showDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
