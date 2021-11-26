package it.cnr.istc.msanbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import java.util.HashMap;
import java.util.Map;

import it.cnr.istc.msanbot.logic.ConnectionEventListener;
import it.cnr.istc.msanbot.logic.EventManager;
import it.cnr.istc.msanbot.logic.FaceType;
import it.cnr.istc.msanbot.logic.RobotEventListener;
import it.cnr.istc.msanbot.logic.Topics;
import it.cnr.istc.msanbot.mqtt.MQTTManager;

public class MainActivity extends TopBaseActivity implements MediaListener, ConnectionEventListener, RobotEventListener {
    SpeechManager speechManager = (SpeechManager)getUnitManager(FuncConstant. SPEECH_MANAGER);
    HardWareManager hardWareManager = (HardWareManager)getUnitManager(FuncConstant.HARDWARE_MANAGER);
    SystemManager systemManager = (SystemManager)getUnitManager(FuncConstant.SYSTEM_MANAGER);
    WheelMotionManager wheelMotionManager= (WheelMotionManager)getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
    LED rageLed = new LED(LED.PART_ALL,LED. MODE_RED,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    LED listeningLed = new LED(LED.PART_ALL,LED. MODE_GREEN,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    LED speechLed = new LED(LED.PART_ALL,LED. MODE_BLUE,(new Integer(10)).byteValue(),(new Integer(3)).byteValue());
    ImageView img;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView textView,mainSpeak,stop;
    Button goForward,goBackward,turnLeft,turnRight, buttonTest;
    ImageView background,serverStatus,recSymbol;
    private AlertDialog tableDialog = null;
    MQTTManager mqttManager = null;
    private Map<String,Boolean> colorCellMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register(MainActivity.class);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

        RobotManager.getInstance().addRobotEventListener(this);
        EventManager.getInstance().addConnectionEventListener(this);

        try {
            recSymbol = findViewById(R.id.recSymbol);
            recSymbol.setVisibility(View.INVISIBLE);

            setContentView(R.layout.activity_main);
            RobotManager.getInstance().setSystemManager(systemManager);
            if (speechManager == null) {
                Toast.makeText(MainActivity.this, "VI SPACCO TUTTO", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(MainActivity.this, "MI AMMAZZO", Toast.LENGTH_LONG).show();
                speechManager.startSpeak("NON SONO NULL");
            }
            initListener();

            goForward = findViewById(R.id.goForwardx);
            goBackward = findViewById(R.id.goBackward);
            turnLeft = findViewById(R.id.turnLeft);
            turnRight = findViewById(R.id.turnRight);
            mainSpeak = findViewById(R.id.button_mainButton_speak);
            buttonTest = findViewById(R.id.buttonTEST);
            stop = findViewById(R.id.button_mainButton_stop);
            stop.setEnabled(false);
            stop.setBackgroundResource(R.drawable.stop_disabled);
            background = findViewById(R.id.background);
            serverStatus = findViewById(R.id.imageView_ServerStatus);
            img = findViewById(R.id.image);

            goForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoAngleWheelMotion noAngleWheelMotion = new NoAngleWheelMotion(
                            NoAngleWheelMotion.ACTION_LEFT_FORWARD, 2, 100
                    );
                    wheelMotionManager.doNoAngleMotion(noAngleWheelMotion);
                    Toast.makeText(MainActivity.this, "s", Toast.LENGTH_SHORT).show();
                }
            });

            goBackward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoAngleWheelMotion noAngleWheelMotion = new NoAngleWheelMotion(
                            NoAngleWheelMotion.ACTION_BACK, 2, 10
                    );
                    wheelMotionManager.doNoAngleMotion(noAngleWheelMotion);
                }
            });

            turnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5, 90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                }
            });

            turnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_RIGHT, 5, 90);
                    wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
                }
            });

            buttonTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MQTTManager.getInstance().disconnect();
                    MQTTManager.getInstance().connect(MainActivity.this);
                    EventManager.getInstance().addConnectionEventListener(MainActivity.this);
                }
            });

            serverStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    final View createPopup = getLayoutInflater().inflate(R.layout.popup_activity, null);
                    dialogBuilder.setView(createPopup);
                    dialog = dialogBuilder.create();

                    EditText newIpEditText = createPopup.findViewById(R.id.newIpEditText);
                    Button setNewIp = createPopup.findViewById(R.id.setBtn);

                    setNewIp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MQTTManager.getInstance().setIp(newIpEditText.getText().toString());
                            talk("Nuovo ip settato", speechLed);
                        }
                    });

                    dialog.show();
                }
            });



            mainSpeak.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    recSymbol.setVisibility(View.VISIBLE);
                    background.setBackgroundColor(android.R.color.black);
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
        } catch (Exception ex) {
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
        recSymbol.setVisibility(View.INVISIBLE);
        stop.setEnabled(false);
        stop.setBackgroundResource(R.drawable.stop_disabled);
    }

    private void initListener() {

            talk("Inizio a sentire",listeningLed);
            stop.setEnabled(false);
            stop.setBackgroundResource(R.drawable.stop_disabled);
            textView = findViewById(R.id.textView);
            hardWareManager.setOnHareWareListener(new InfrareListener() {
                @Override
                public void infrareDistance(int part, int distance) {

                }
            });

            speechManager.setOnSpeechListener(new RecognizeListener() {
                @Override
                public void onRecognizeText(RecognizeTextBean recognizeTextBean) {
                    recSymbol.setVisibility(View.INVISIBLE);
                    String text = recognizeTextBean.getText().toLowerCase();
                    MQTTManager.getInstance().publish(Topics.CHAT.getTopic() + "/" + MQTTManager.getInstance().getId(), text);
                    textView.setText(recognizeTextBean.getText());
                    stop.setEnabled(true);
                    stop.setBackgroundResource(R.drawable.stop);
                    if (text.contains("ciao")) {
                        long time = new Date().getTime();
                        if (time % 2 == 1) {
                            talk("Ciao",speechLed);
                        } else {
                            talk("lei è molto cortese",speechLed);
                        }
                    }
                    if(text.contains("prova")){
                        talk("prova discorso lungo lunghissimissssssimo",rageLed);
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
       //showImage("https://publications.cnr.it/api/v1/author/image/luca.coraci");
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
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View createPopup = getLayoutInflater().inflate(R.layout.popup_activity, null);
        dialogBuilder.setView(createPopup);
        dialog = dialogBuilder.create();
        ImageView image = createPopup.findViewById(R.id.image);
        Glide.with(this).load(url).into(image);
        dialog.show();

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
        //Intent popupwindow = new Intent(MainActivity.this, LinkPopUpWindow.class);
        //startActivity(popupwindow);
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View createPopup = getLayoutInflater().inflate(R.layout.popup_activity, null);
        dialogBuilder.setView(createPopup);
        dialog = dialogBuilder.create();
        TextView linkPress = createPopup.findViewById(R.id.linkPress);
        String newLink;
        if(!(linkPress.getText().toString().startsWith("http"))){
            newLink = "http://" + link;
        }
        else{
            newLink = link;
        }

        linkPress.setText(newLink);

        linkPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse((newLink))));
            }
        });



        dialog.show();
    }

    /**
     * Mostra un video con il link di youtube
     * @param url
     * il link da mostrare
     */
    public void showYouTubeVideo(String url) {
        /*
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View createPopup = getLayoutInflater().inflate(R.layout.popup_activity, null);
        dialogBuilder.setView(createPopup);
        dialog = dialogBuilder.create();
        VideoView videoView = createPopup.findViewById(R.id.video);
        videoView.setVideoURI(Uri.parse(url));

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        dialog.show();

         */
        if(isYouTubeLink(url))
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse((url))));
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Context dialogContext = builder.getContext();
        LayoutInflater inflater = LayoutInflater.from(dialogContext);
        View alertView = inflater.inflate(R.layout.table_dialog, null);
        builder.setView(alertView);
        TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.tableLayout);
        int row = 0;
        boolean continueTable = false;
        for( String d : tabella){
            String[] split = d.split("<CELL>");
            TableRow tableRow = new TableRow(dialogContext);
            tableRow.setPadding(3,3,3,3);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                    (0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.setMargins(3,3,3,3);
            tableRow.setLayoutParams(layoutParams);

            for (final String cella : split) {
                final TextView textView1 = new TextView(dialogContext);
                final String cellText;
                if(row == 0){
                    textView1.setTypeface(null, Typeface.BOLD);
                    if(cella.contains("<CONTINUE>")){
                        cellText = cella.replace("<CONTINUE>","");
                        continueTable =true;
                    }else{
                        cellText = cella;
                    }
                }else{
                    cellText = cella;
                    GradientDrawable gd = new GradientDrawable(
                            //  GradientDrawable.Orientation.TOP_BOTTOM,
                            // new int[] {0xFFe5edef,0xFFcedde0});
                    );
                    // gd.setCornerRadius(6);
                    gd.setColor(0xFFe9eca0);  // #e9eca0
                    gd.setStroke(1, 0xFF000000);
                    textView1.setBackground(gd);
                }
                textView1.setPadding(5,5,5,5);
                // textView1.setLayoutParams(new TableRow.LayoutParams
                //         (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                textView1.setTextSize(18);
                textView1.setText(cellText);
                colorCellMap.put(""+cellText,Boolean.FALSE);
                if(row != 0) {
                    textView1.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (colorCellMap.get("" + cellText)) {
                                                             System.out.println("TRUE");
                                                             textView1.setBackgroundColor(0xFFFFFFFF);
                                                             colorCellMap.put("" + cellText, Boolean.FALSE);
                                                         } else {
                                                             System.out.println("FALSE");
                                                             textView1.setBackgroundColor(0xFF00FF00);
                                                             colorCellMap.put("" + cellText, Boolean.TRUE);
                                                         }

                                                     }
                                                 }

                    );
                }


                tableRow.addView(textView1,layoutParams);

            }
            row++;
            tableLayout.addView(tableRow);
        }
        builder.setCancelable(true);
        //AlertDialog alertDialog =

        if(!continueTable){
            if(this.tableDialog != null){
                this.tableDialog.cancel();
                this.tableDialog.dismiss();
                this.tableDialog = builder.create();
            }else{
                this.tableDialog = builder.create();
            }
            tableDialog.show();
        }else{
            AlertDialog tempTable = builder.create();
            tempTable.show();
        }

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
        NoAngleWheelMotion noAngleWheelMotion = new NoAngleWheelMotion(NoAngleWheelMotion.ACTION_RESET, 1);
    }

    @Override
    public void serverOnline() {
        talk("Server Online",speechLed);
        //TextView serverStatus = findViewById(R.id.imageView_ServerStatus);
        serverStatus.setColorFilter(Color.argb(255, 0, 255, 0));
    }

    @Override
    public void serverOffline() {
        talk("Server Offline",speechLed);
        //TextView serverStatus = findViewById(R.id.imageView_ServerStatus);
        serverStatus.setColorFilter(Color.argb(255, 0, 255, 0));
    }

    @Override
    public void speak(String text) {
        talk(text, speechLed);
    }

    @Override
    public void FaceChanged(FaceType face) {
        switch (face){
            case SAD:
                background.setBackgroundResource(R.drawable.cry);
            break;
            case LOVE:
                background.setBackgroundResource(R.drawable.love);
            break;
            case OUTRAGE:
                background.setBackgroundResource(R.drawable.flame);
            break;
        }
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
