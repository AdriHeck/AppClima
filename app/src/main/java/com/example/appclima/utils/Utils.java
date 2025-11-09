package com.example.appclima.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Utils {

    public static void mostrarDialogErro(Activity activity, String titulo, String mensagem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
