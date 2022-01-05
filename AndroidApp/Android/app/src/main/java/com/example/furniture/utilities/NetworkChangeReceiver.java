package com.example.furniture.utilities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import androidx.appcompat.app.AlertDialog;


public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please check your connection and try again");
        AlertDialog alertDialog = builder.create();
        int status = NetworkUtil.getConnectivityStatus(context);


        if (status == NetworkUtil.TYPE_NOT_CONNECTED) {

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);

            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Open setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(dialogIntent);
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

        } else {
            alertDialog.dismiss();
        }
    }
}
