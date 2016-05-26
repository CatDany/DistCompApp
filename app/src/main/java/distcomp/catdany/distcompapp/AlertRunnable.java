package distcomp.catdany.distcompapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Dany on 26.05.2016.
 */
public class AlertRunnable implements Runnable {

    public final Activity context;
    public final AlertDialog dialog;

    public AlertRunnable(Activity context, AlertDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    public void show() {
        context.runOnUiThread(this);
    }

    @Override
    public void run() {
        dialog.show();
    }
}
