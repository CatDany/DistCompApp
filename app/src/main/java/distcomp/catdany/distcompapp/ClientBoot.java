package distcomp.catdany.distcompapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import catdany.bfdist.client.BFClient;
import catdany.bfdist.log.BFLog;

/**
 * Created by Dany on 26.05.2016.
 */
public class ClientBoot implements Runnable, View.OnClickListener {

    private static final String prefUUID = "DistComp-Client-UUID";

    public final MainActivity context;

    public ClientBoot(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        new Thread(this, "Client-Boot").start();
    }

    public void run() {
        try {
            //
        } catch (Exception t) {

        }
        String ip = ((EditText)context.findViewById(R.id.textIP)).getText().toString();
        String port = ((EditText)context.findViewById(R.id.textPort)).getText().toString();
        UUID id = UUID.randomUUID();
        SharedPreferences pref = context.getPreferences(Context.MODE_PRIVATE);
        if (pref.contains(prefUUID)) {
            id = UUID.fromString(pref.getString(prefUUID, null));
        } else {
            pref.edit().putString(prefUUID, id.toString()).commit();
        }
        BFLog.i("UUID: %s", id);
        try {
            BFClient.instantiate(id, InetAddress.getByName(ip), Integer.parseInt(port));
            context.startProgressActivity();
        } catch (UnknownHostException t) {
            BFLog.t(t);
        }
    }
}
