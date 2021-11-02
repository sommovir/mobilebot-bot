package it.cnr.istc.msanbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.EmotionsType;
import com.sanbot.opensdk.function.beans.LED;
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.StreamOption;
import com.sanbot.opensdk.function.beans.speech.Grammar;
import com.sanbot.opensdk.function.beans.speech.RecognizeTextBean;
import com.sanbot.opensdk.function.beans.speech.SpeakStatus;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.unit.HDCameraManager;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.MediaManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.hardware.InfrareListener;
import com.sanbot.opensdk.function.unit.interfaces.media.MediaListener;
import com.sanbot.opensdk.function.unit.interfaces.media.MediaStreamListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.RecognizeListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeakListener;

import java.util.Date;

public class MainActivity extends TopBaseActivity implements MediaListener{
    //pecilli zan
    SpeechManager speechManager = (SpeechManager)getUnitManager(FuncConstant. SPEECH_MANAGER);
    HardWareManager hardWareManager = (HardWareManager)getUnitManager(FuncConstant.HARDWARE_MANAGER);
    WheelMotionManager wheelMotionManager= (WheelMotionManager)getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
    LED rageLed = new LED(LED.PART_ALL,LED. MODE_RED,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    LED listeningLed = new LED(LED.PART_ALL,LED. MODE_GREEN,(new Integer(25)).byteValue(),(new Integer(3)).byteValue());
    LED speechLed = new LED(LED.PART_ALL,LED. MODE_BLUE,(new Integer(25)).byteValue(),(new Integer(3)).byteValue());

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            register(MainActivity.class);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            if (speechManager == null) {
                Toast.makeText(MainActivity.this, "VI SPACCO TUTTO", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "MI AMMAZZO", Toast.LENGTH_LONG).show();
                speechManager.doWakeUp();
                speechManager.startSpeak("NON SONO NULL");
            }
            initListener();

            Button button;

            button = findViewById(R.id.button_mainButton_speak);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //speechManager.startSpeak("Uga Buga Uga Tunga");
                    //systemManager.showEmotion(EmotionsType.SMILE);
                    speechManager.doWakeUp();
                    //initListener();

        /*speechManager.setOnSpeechListener(new RecognizeListener() {
            @Override
            public boolean onRecognizeResult(Grammar grammar) {
                String x = grammar.getText();
                Toast.makeText(MainActivity.this, x, Toast.LENGTH_SHORT).show();
                speechManager.startSpeak("hai detto" + x);
                if (x.equals("ciao")) {
                    speechManager.startSpeak("Mi hai salutato");
                    return true;
                }
                return false;
            }

            @Override
            public void onRecognizeText(RecognizeTextBean recognizeTextBean) {
                speechManager.startSpeak("onRecognizeText");
                Toast.makeText(MainActivity.this, recognizeTextBean.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRecognizeVolume(int i) {
                speechManager.startSpeak("onRecognizeVolume" + i);
                Toast.makeText(MainActivity.this, "Volume" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartRecognize() {
                speechManager.startSpeak("onStartRecognize");
                Toast.makeText(MainActivity.this, "Inizio speak", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopRecognize() {
                speechManager.startSpeak("onStopRecognize");
                Toast.makeText(MainActivity.this, "Fine speak", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, int i1) {
                speechManager.startSpeak("onStopRecognize");
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });*/

                }
            });
        }catch(Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initListener() {

            talk("Inizio a sentire",listeningLed);

            textView = findViewById(R.id.textView);
            hardWareManager.setOnHareWareListener(new InfrareListener() {
                @Override
                public void infrareDistance(int part, int distance) {

                }
            });

            speechManager.setOnSpeechListener(new RecognizeListener() {
                @Override
                public void onRecognizeText(RecognizeTextBean recognizeTextBean) {
                    String text = recognizeTextBean.getText().toLowerCase();
                    textView.setText(recognizeTextBean.getText());

                    speechManager.startSpeak("Primo");

                    if (text.contains("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            talk("Ciao",speechLed);
                        } else {
                            talk("lei è molto cortese",speechLed);
                        }
                    }
                    if(text.equals("girati")){
                        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5,180);
                        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                    }

                    //if(FaceManager.)Simo fai a singletone per gettare le faccie

                    hardWareManager.setLED(rageLed);

                }

                @Override
                public void onStopRecognize() {
                    Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
                    System.out.println("stop recognize");
                }

                @Override
                public void onStartRecognize() {
                    Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();
                    System.out.println("start recognize");
                }

                @Override
                public void onRecognizeVolume(int i) {
                    System.out.println("Problema al volume");
                }

                @Override
                public void onError(int i, int i1) {
                    Toast.makeText(MainActivity.this, "err", Toast.LENGTH_SHORT).show();
                    System.out.println("error " + i + " " + i1);
                }

                @Override
                public boolean onRecognizeResult(@NonNull Grammar grammar) {
                    //只有在配置了RECOGNIZE_MODE为1，且返回为true的情况下，才会拦截

                    String text = grammar.getText().toLowerCase();
                    textView.setText(grammar.getText());

                    speechManager.startSpeak("Secondo");

                    if (text.contains("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            talk("Ciao",speechLed);
                        } else {
                            talk("lei è molto cortese",speechLed);
                        }
                    }
                    if(text.equals("girati")){
                        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion( RelativeAngleWheelMotion.TURN_LEFT, 5,180);
                        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                    }
                    return true;
                }
            });
        }



    @Override
    protected void onMainServiceConnected() {
        speechManager.startSpeak("Sono connesso");
    }

    public void talk(String text,LED led){
        speechManager.startSpeak(text);
        hardWareManager.setLED(led);
    }
}
