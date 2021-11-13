package it.cnr.istc.msanbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sanbot.opensdk.base.TopBaseActivity;
import com.sanbot.opensdk.beans.FuncConstant;
import com.sanbot.opensdk.beans.OperationResult;
import com.sanbot.opensdk.function.beans.LED;
import com.sanbot.opensdk.function.beans.speech.Grammar;
import com.sanbot.opensdk.function.beans.speech.RecognizeTextBean;
import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;
import com.sanbot.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.sanbot.opensdk.function.unit.HardWareManager;
import com.sanbot.opensdk.function.unit.SpeechManager;
import com.sanbot.opensdk.function.unit.SystemManager;
import com.sanbot.opensdk.function.unit.WheelMotionManager;
import com.sanbot.opensdk.function.unit.interfaces.hardware.InfrareListener;
import com.sanbot.opensdk.function.unit.interfaces.media.MediaListener;
import com.sanbot.opensdk.function.unit.interfaces.speech.RecognizeListener;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class MainActivity extends TopBaseActivity implements MediaListener{
    //pecilli zan
    SpeechManager speechManager = (SpeechManager)getUnitManager(FuncConstant. SPEECH_MANAGER);
    HardWareManager hardWareManager = (HardWareManager)getUnitManager(FuncConstant.HARDWARE_MANAGER);
    SystemManager systemManager = (SystemManager)getUnitManager(FuncConstant.SYSTEM_MANAGER);
    WheelMotionManager wheelMotionManager= (WheelMotionManager)getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
    LED rageLed = new LED(LED.PART_ALL,LED. MODE_RED,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    LED listeningLed = new LED(LED.PART_ALL,LED. MODE_GREEN,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    LED speechLed = new LED(LED.PART_ALL,LED. MODE_BLUE,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    ImageView img;

    TextView textView,mainSpeak,stop;
    Button goForward,goBackward,turnLeft,turnRight;
    private AlertDialog tableDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            register(MainActivity.class);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);
            RobotManager.getInstance().setSystemManager(systemManager);
            if (speechManager == null) {
                Toast.makeText(MainActivity.this, "VI SPACCO TUTTO", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(MainActivity.this, "MI AMMAZZO", Toast.LENGTH_LONG).show();
                speechManager.startSpeak("NON SONO NULL");
            }
            initListener();

            goForward = findViewById(R.id.goForward);
            goBackward = findViewById(R.id.goBackward);
            turnLeft = findViewById(R.id.turnLeft);
            turnRight = findViewById(R.id.turnRight);
            mainSpeak = findViewById(R.id.button_mainButton_speak);
            stop = findViewById(R.id.button_mainButton_stop);
            stop.setEnabled(false);
            stop.setHighlightColor(000000);
            img = findViewById(R.id.image);

            goForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoAngleWheelMotion noAngleWheelMotion = new NoAngleWheelMotion(
                            NoAngleWheelMotion.ACTION_FORWARD, 5,1000
                    );
                    wheelMotionManager.doNoAngleMotion(noAngleWheelMotion);
                }
            });

            goBackward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoAngleWheelMotion noAngleWheelMotion = new NoAngleWheelMotion(
                            NoAngleWheelMotion.ACTION_BACK, 2,10
                    );
                    wheelMotionManager.doNoAngleMotion(noAngleWheelMotion);
                }
            });

            turnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5,90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                }
            });

            turnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_RIGHT, 5,90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                }
            });

            mainSpeak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //speechManager.startSpeak("Uga Buga Uga Tunga");
                    //systemManager.showEmotion(EmotionsType.SMILE);
                    speechManager.doWakeUp();
                    //listenWhenToSpeak();
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

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //speechManager.startSpeak("Sto zitt");
                    stop();
                    Toast.makeText(MainActivity.this, "Fine", Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

@Deprecated
    private void listenWhenToSpeak(){
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    while (true) {
                        if (speechManager != null) {
                            OperationResult speaking = speechManager.isSpeaking();
                            if (speaking != null) {
                                String result = speaking.getResult();
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "speaking is nullone", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
        }catch (Throwable ex){
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void stop(){
        new Handler().postDelayed(() -> {
            speechManager.startSpeak("Ok basta");
        },0);
        stop.setEnabled(false);
        stop.setHighlightColor(000000);
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
                    stop.setEnabled(true);
                    if (text.contains("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            talk("Ciao",speechLed);
                        } else {
                            talk("lei è molto cortese",speechLed);
                        }
                    }
                    if(text.contains("lui")){
                        talk("Combattenti di terra, di mare e dell'aria! Camicie nere della rivoluzione e delle legioni! Uomini e donne d'Italia, dell'Impero e del regno d'Albania! Ascoltate!\n" +
                                "\n" +
                                "L'ora segnata dal destino batte nel cielo della nostra patria. L'ora delle decisioni irrevocabili. La dichiarazione di guerra è già stata consegnata agli ambasciatori di Gran Bretagna e di Francia.\n" +
                                "\n" +
                                "Scendiamo in campo contro le democrazie plutocratiche e reazionarie dell'Occidente, che, in ogni tempo, hanno ostacolato la marcia, e spesso insidiato l'esistenza medesima del popolo italiano.\n" +
                                "\n" +
                                "Alcuni lustri della storia più recente si possono riassumere in queste parole: frasi, promesse, minacce, ricatti e, alla fine, quale coronamento dell'edificio, l'ignobile assedio societario di cinquantadue stati. La nostra coscienza è assolutamente tranquilla. Con voi il mondo intero è testimone che l'Italia del Littorio ha fatto quanto era umanamente possibile per evitare la tormenta che sconvolge l'Europa; ma tutto fu vano.",rageLed);
                    }
                    if(text.equals("girati")){
                        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5,180);
                        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                    }

                    //
                    // speechManager.doWakeUp();

                    //if(FaceManager.)Simo fai a singletone per gettare le faccie

                    //hardWareManager.setLED(rageLed);
                    speechManager.doWakeUp();
                }

                @Override
                public void onStopRecognize() {
                    Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
                    System.out.println("stop recognize");

                    //speechManager.doWakeUp();
                }

                @Override
                public void onStartRecognize() {
                    Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();
                    System.out.println("start recognize");
                }

                @Override
                public void onRecognizeVolume(int i) {
                    //Toast.makeText(MainActivity.this, "Vol", Toast.LENGTH_SHORT).show();
                    System.out.println("Problema al volume");
                }

                @Override
                public void onError(int i, int i1) {
                    Toast.makeText(MainActivity.this, "err", Toast.LENGTH_SHORT).show();
                    System.out.println("error " + i + " " + i1);
                }

                @Override
                public boolean onRecognizeResult(@NonNull Grammar grammar) {
                    Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
                    //只有在配置了RECOGNIZE_MODE为1，且返回为true的情况下，才会拦截

                    String text = grammar.getText().toLowerCase();
                    textView.setText(grammar.getText());

                    if(text.contains("schifo")){
                        speechManager.doWakeUp();
                    }

                    /*speechManager.startSpeak("Secondo");


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
                    }*/
                    return true;
                }
            });

        }

    @Override
    protected void onMainServiceConnected() {
       //listenWhenToSpeak();
       speechManager.startSpeak("Sono connesso");
       showImage("https://publications.cnr.it/api/v1/author/image/luca.coraci");
    }

    public void talk(String text,LED led){
        speechManager.startSpeak(text);
        hardWareManager.setLED(led);
    }

    /**
     * Sintetizza il testo text,
     * @param text
     * il testo da sintetizzare
     * @param autolisten
     * se true effettua l'autolisten
     */
    public void speakText(String text, boolean autolisten) {
        talk(text,speechLed);
        if(autolisten){
            speechManager.doWakeUp();
        }
    }

    /**
     * Mosra a schermo la multichoice nel formato standardizzato del comando *multichoice*
     * @param text
     * il comando (sintassi da ritrovare, ndr)
     */
    public void showTestChoice(String text) {
    }

    /**
     * Mostra un video hardcoded in autoplay
     */
    public void showVideo() {
    }

    /**
     * Mostra una immagine hardcoded in autoplay
     */
    public void showImage() {
    }

    /**
     * Mostra un immagine pubblicata in rete
     * @param url
     * l'URL pubblico dell'immagine da mostrare
     */
    public void showImage(String url) {
        Glide.with(this).load(url).into(img);

        /*
        Intent idd = new Intent(MainActivity.this, ImageDisplayerDialog.class);
        FaceActivity.this.startActivity(idd);*/

    }

    /**
     * Attiva l'ascolto dopo un certo numero di millisecondi
     * @param time
     * il numero di millisecondi da attendere prima di mettersi in ascolto
     */
    public void listenAt(Long time) {
    }

    /**
     * mostra a schermo un link
     * @param link
     * il link da mostrare, deve essere cliccabile
     */
    public void showLink(String link) {

    }

    /**
     * Mostra un video con il link di youtube
     * @param link
     * il link da mostrare
     */
    public void showYouTubeVideo(String link) {

    }

    /**
     * Verifica che il link in esame sia un link di youtube
     * @param link
     * il link da controllare. Se il link è "link farlocco" deve eseguire una procedura di test
     * di un link certamente valido hard-coded.
     * @return
     * true se è un link da youtube, false altrimenti
     */
    private boolean isYouTubeLink(String link){
        if(link.startsWith("https://www.youtube.com")) {
            return true;
        }return false;
    }

    /**
     * Mostra una tabella a schermo
     * @param tabella
     * Un array di righe secondo il formato di app-text
     */
    public void showGenericTable(String[] tabella) {
    }

    /**
     * setta la grafica di modo che mostri di essere online
     */
    public void forceServerOnline() {
    }

    /**
     * Resetta tutte le impostazioni grafiche e robotiche nella posizione di default
     * @param backToNormalTime
     * numero di millisecondi da attendere prima del ritorno al normal-time
     */
    public void setBackToNormalTime(Long backToNormalTime) {
    }

    /*public void showTableData(String[] data){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informazioni");

        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.table_dialog, null);
        builder.setView(alertView);



        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.tableLayout);
        for( String d : data){

            String[] split = d.split("<CELL>");
            String title = split[0];
            String infoinfo = split[1];

            TableRow tableRow = new TableRow(dialogContext);
            tableRow.setPadding(10,10,10,10);
            tableRow.setLayoutParams(new TableRow.LayoutParams
                    (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

            TextView textView1 = new TextView(dialogContext);
            textView1.setPadding(15,15,15,15);
            // textView1.setLayoutParams(new TableRow.LayoutParams
            //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            textView1.setTextSize(18);
            textView1.setText(title);
            tableRow.addView(textView1);

            TextView textView2 = new TextView(dialogContext);
            textView2.setPadding(15,15,15,15);
            // textView2.setLayoutParams(new TableRow.LayoutParams
            //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
            textView2.setTextSize(18);
            textView2.setText(infoinfo);
            textView2.setBackgroundResource(R.color.row2);
            tableRow.addView(textView2);

            tableLayout.addView(tableRow);
        }

        builder.setCancelable(true);
        if(this.tableDialog != null){
            this.tableDialog.cancel();
            this.tableDialog.dismiss();
            this.tableDialog = builder.create();
        }else{
            this.tableDialog = builder.create();
        }

        tableDialog.show();
        // AlertDialog alertDialog = builder.create();
        //  alertDialog.show();
    }*/

}
