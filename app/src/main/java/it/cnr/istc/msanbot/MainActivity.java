package it.cnr.istc.msanbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.sanbot.opensdk.base.TopBaseActivity;

public class MainActivity extends TopBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onMainServiceConnected() {
        System.out.println("sono qui");
        Toast.makeText(this,"ciao",Toast.LENGTH_LONG).show();
    }
}