package it.cnr.istc.msanbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import com.sanbot.opensdk.function.beans.SpeakOption;
import com.sanbot.opensdk.function.beans.StreamOption;
import com.sanbot.opensdk.function.beans.speech.Grammar;
import com.sanbot.opensdk.function.beans.speech.RecognizeTextBean;
import com.sanbot.opensdk.function.beans.speech.SpeakStatus;
import com.sanbot.opensdk.function.unit.HDCameraManager;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.MediaManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.interfaces.hardware.InfrareListener;
import com.sanbot.opensdk.function.unit.interfaces.media.MediaListener;
import com.sanbot.opensdk.function.unit.interfaces.media.MediaStreamListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.RecognizeListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.SpeakListener;

import java.util.Date;

public class MainActivity extends TopBaseActivity implements MediaListener{

    SpeechManager speechManager = (SpeechManager)getUnitManager(FuncConstant. SPEECH_MANAGER);
    HardWareManager hardWareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);
    SystemManager systemManager= (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
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
            speechManager.startSpeak("Inizio a sentire");

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

                    if (text.contains("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            speechManager.startSpeak("Ciao");
                        } else {
                            speechManager.startSpeak("lei è molto cortese");
                        }
                    }
                    if ((text.contains("poesia") && text.contains("risorgimento")) || (text.contains("poesia") && text.contains("risorgimentale"))) {
                        long time = new Date().getTime(); //Il Barone Bettino Ricasoli, secondo presidente del Consiglio del Regno d'Italia fece servire a Vittorio Emanuele secondo, la panzanella ? Cos'è la panzanella ?
                        speechManager.startSpeak("Certamente. Ne conosco una di Aldo Fabrizi sulla panzanella. Ascolta.  E che ce vo’\n" +
                                "pe’ fa’ la Panzanella?\n" +
                                "Nun è ch’er condimento sia un segreto,\n" +
                                "oppure è stabbilito da un decreto,\n" +
                                "però la qualità dev’esse quella.\n" +
                                "In primise: acqua fresca de cannella,\n" +
                                "in secondise: ojo d’uliveto,\n" +
                                "e come terzo: quer di-vino aceto\n" +
                                "che fa’ venì la febbre magnarella.\n" +
                                "Pagnotta paesana un po’ intostata,\n" +
                                "cotta all’antica,co’ la crosta scura,\n" +
                                "bagnata fino a che nun s’è ammollata.\n" +
                                "In più, per un boccone da signori,\n" +
                                "abbasta rifinì la svojatura\n" +
                                "co’ basilico, pepe e pommidori.");
                    }
                    if ((text.contains("ballo") && text.contains("risorgimento")) ||
                            (text.contains("danza") && text.contains("risorgimento")) ||
                            (text.contains("ballo") && text.contains("risorgimentale")) ||
                            (text.contains("danza") && text.contains("risorgimentale")) ||
                            (text.contains("musica") && text.contains("risorgimento")) ||
                            (text.contains("musica") && text.contains("risorgimentale"))

                    ) {
                        speechManager.startSpeak("Si, senti questo valzer, premi il tasto play");
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "CmAH4GFExiw"));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/embed/CmAH4GFExiw"));
                        // Uri.parse("https://www.m.youtube.com//watch?v=yKwg4MLuwXs"));
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }
                    }
                    if ((text.contains("vino") && text.contains("garibaldi"))

                    ) {
                        speechManager.startSpeak("In primo luogo, Garibaldi era astemio, e più che bere gli piaceva mangiare. Il suo piatto preferito era pane e pecorino, accompagnato da fave fresche di stagione");

                    }
                    if ((text.contains("cavur") || text.contains("cavour"))

                    ) {
                        speechManager.startSpeak("Sai cosa diceva cavour. che cattura più amici la mensa che la mente");

                    }

                    if ((text.contains("vittorio") && text.contains("emanuele"))

                    ) {
                        speechManager.startSpeak("so solo che c'è una fermata della metro");

                    }
                    if ((text.contains("mazzini"))

                    ) {
                        speechManager.startSpeak("Oh, di mazzini ne so una, ascolta cosa diceva sul cioccolato. Il cioccolato ha mille pregi. Consola dai fallimenti, dai tradimenti, dalle ingiurie della vita, dalla malinconia per le passioni perdute e per quelle mai avute");

                    }
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

                    if (text.equals("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            speechManager.startSpeak("Saluti a lei");
                        } else {
                            speechManager.startSpeak("lei è molto cortese");
                        }
                    }
                    if ((text.contains("poesia") && text.contains("risorgimento")) || (text.contains("poesia") && text.contains("risorgimentale"))) {
                        long time = new Date().getTime(); //Il Barone Bettino Ricasoli, secondo presidente del Consiglio del Regno d'Italia fece servire a Vittorio Emanuele secondo, la panzanella ? Cos'è la panzanella ?
                        speechManager.startSpeak("Certamente. Ne conosco una di Aldo Fabrizi sulla panzanella. Ascolta.  E che ce vo’\n" +
                                "pe’ fa’ la Panzanella?\n" +
                                "Nun è ch’er condimento sia un segreto,\n" +
                                "oppure è stabbilito da un decreto,\n" +
                                "però la qualità dev’esse quella.\n" +
                                "In primise: acqua fresca de cannella,\n" +
                                "in secondise: ojo d’uliveto,\n" +
                                "e come terzo: quer di-vino aceto\n" +
                                "che fa’ venì la febbre magnarella.\n" +
                                "Pagnotta paesana un po’ intostata,\n" +
                                "cotta all’antica,co’ la crosta scura,\n" +
                                "bagnata fino a che nun s’è ammollata.\n" +
                                "In più, per un boccone da signori,\n" +
                                "abbasta rifinì la svojatura\n" +
                                "co’ basilico, pepe e pommidori.");
                    }
                    if ((text.contains("ballo") && text.contains("risorgimento")) ||
                            (text.contains("danza") && text.contains("risorgimento")) ||
                            (text.contains("ballo") && text.contains("risorgimentale")) ||
                            (text.contains("danza") && text.contains("risorgimentale")) ||
                            (text.contains("musica") && text.contains("risorgimento")) ||
                            (text.contains("musica") && text.contains("risorgimentale"))

                    ) {
                        speechManager.startSpeak("Si, senti questo valzer, premi il tasto play");
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "CmAH4GFExiw"));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/embed/CmAH4GFExiw"));
                        // Uri.parse("https://www.m.youtube.com//watch?v=yKwg4MLuwXs"));
                        try {
                            startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            startActivity(webIntent);
                        }
                    }
                    if ((text.contains("vino") && text.contains("garibaldi"))

                    ) {
                        speechManager.startSpeak("In primo luogo, Garibaldi era astemio, e più che bere gli piaceva mangiare. Il suo piatto preferito era pane e pecorino, accompagnato da fave fresche di stagione");

                    }
                    if ((text.contains("cavur") || text.contains("cavour"))

                    ) {
                        speechManager.startSpeak("Sai cosa diceva cavour. che cattura più amici la mensa che la mente");

                    }

                    if ((text.contains("vittorio") && text.contains("emanuele"))

                    ) {
                        speechManager.startSpeak("so solo che c'è una fermata della metro");

                    }
                    if ((text.contains("mazzini"))

                    ) {
                        speechManager.startSpeak("Oh, di mazzini ne so una, ascolta cosa diceva sul cioccolato. Il cioccolato ha mille pregi. Consola dai fallimenti, dai tradimenti, dalle ingiurie della vita, dalla malinconia per le passioni perdute e per quelle mai avute");

                    }
                    return true;
                }
            });
        }



    @Override
    protected void onMainServiceConnected() {

    }
}