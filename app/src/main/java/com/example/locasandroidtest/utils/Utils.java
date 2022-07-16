package com.example.locasandroidtest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;

import java.io.IOException;
import java.io.InputStream;
public class Utils {
  public static String getJsonFromAssets(Context context, String fileName) {
    String jsonString;
    try {
      InputStream is = context.getAssets().open(fileName);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      jsonString = new String(buffer, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return jsonString;
  }

  public static AlertDialog showCustomDialog(Activity context, String title, String message, String firstButtonName, String seconButtonName, String from, boolean isCancellable, OnDialogbuttonClickListner onDialogbuttonClickListner) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(firstButtonName, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                if (onDialogbuttonClickListner != null) {
                  onDialogbuttonClickListner.onButtonYesClick(from);
                }
                dialog.dismiss();
              }
            }).setNegativeButton(seconButtonName, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                if (onDialogbuttonClickListner != null) {
                  onDialogbuttonClickListner.onButtonNoClick(from);
                }
                dialogInterface.dismiss();
              }
            })
            .setCancelable(isCancellable);
    return builder.show();
  }

  public static void showCustomDialog(Activity context, String title, String message, String firstButtonName, String seconButtonName, String from, OnDialogbuttonClickListner onDialogbuttonClickListner) {
    showCustomDialog(context, title, message, firstButtonName, seconButtonName, from, false, onDialogbuttonClickListner);
  }

  public interface OnDialogbuttonClickListner {
    void onButtonYesClick(String from);

    default void onButtonNoClick(String from) {
    }
  }
}