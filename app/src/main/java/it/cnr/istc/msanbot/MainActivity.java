package it.cnr.istc.msanbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;

public class MainActivity extends TopBaseActivity {

    SpeechManager speechManager = (SpeechManager)getUnitManager(FuncConstant. SPEECH_MANAGER);
    SystemManager systemManager= (SystemManager)getUnitManager(FuncConstant. SYSTEM_MANAGER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button;

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechManager.startSpeak("Se il sole bacia i belli");
                systemManager.showEmotion(EmotionsType.SMILE);
            }
        });
    }

    @Override
    protected void onMainServiceConnected() {
        speechManager.startSpeak("Sono connesso");
    }



}