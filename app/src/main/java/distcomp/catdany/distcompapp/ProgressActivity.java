package distcomp.catdany.distcompapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import catdany.bfdist.client.BFClient;
import catdany.bfdist.client.ServerCom;

public class ProgressActivity extends AppCompatActivity implements View.OnClickListener {

    public static ProgressActivity activityInstance;
    public boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ((Button)findViewById(R.id.buttonStop)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonHome)).setOnClickListener(this);
        activityInstance = this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStop) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BFClient.getClient().getServerCom().stop();
                }
            }, "ServerComStopper").start();
            finish();
        } else if (v.getId() == R.id.buttonHome) {
            Intent intentHome = new Intent(Intent.ACTION_MAIN);
            intentHome.addCategory(Intent.CATEGORY_HOME);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
        }
    }

    public void setNumberInProgress(final String number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.textCurrentNumber)).setText(number);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.isPaused = false;
    }

    @Override
    public void onBackPressed() {}
}
