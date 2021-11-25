package it.cnr.istc.msanbot.mqtt;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.PrintWriter;
import java.io.StringWriter;

import it.cnr.istc.msanbot.logic.ConnectionEventListener;
import it.cnr.istc.msanbot.logic.EventManager;
import it.cnr.istc.msanbot.logic.Topics;

/**
 * Created by Luca Coraci [luca.coraci@istc.cnr.it] on 18/06/2020.
 */
public class MQTTManager {

    private static MQTTManager _instance = null;
    String clientId = "robottino";
    // MqttAndroidClient client = null;
    private final String IM_ALIVE_TOPIC = "imalive";
    private static final String SPEECH_TOPIC = "speech";
    private static final String MOVE_TOPIC = "move"; //direction:speed:quantity
    private static final String DIRECTION_FORWARD = "forward";
    private static final String TAKE_PIC = "takepic";
    private static final String RECEIVE_PIC = "receive_pic";
    private static String ip;
    private Context context;
    MqttClient client = null;


    private MQTTManager(){
        clientId = MqttClient.generateClientId();
        System.out.println("client id = "+clientId);
    }

    public static MQTTManager getInstance(){
        if(_instance == null){
            _instance = new MQTTManager();
        }
        return _instance;
    }

    public void setIp(String newIp){
        ip = newIp;
    }



    public void connect(final Context context) {
        try {
            this.context = context;
            client = new MqttClient("tcp://" + ip + ":1883", clientId, new MemoryPersistence());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            MqttConnectOptions opt = new MqttConnectOptions();
            opt.setCleanSession(true);
            opt.setAutomaticReconnect(true);

            client.connect(opt);

            subscribe();

            imalive();

            EventManager.getInstance().serverOnline();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Non riesco a connettermi");
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            System.out.println("Non va");
            Toast toast = Toast.makeText(context, errors.toString(), Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();
        }
    }



           /* IMqttToken token = client.connect();

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Log.d(TAG, "onSuccess");
                    System.out.println("on Success");
                    try{

                        EventManager.getInstance().speak("sto per inviare un test");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        imalive();
                        //EventManager.getInstance().speak("segnale inviato");

                        //Thread.sleep(2000);

                        //EventManager.getInstance().speak("Mi sono connesso");

                        //Thread.sleep(2000);

                        subscribe();


                    } catch (Exception e) {
                        e.printStackTrace();
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        EventManager.getInstance().speak("non funziona una ceppa");
                        Toast toast=Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG);
                        toast.setMargin(50,50);
                        toast.show();

                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    System.out.println("on Failure");

                }
            });*/




    //SUBSCRIBE PHASE





    private void subscribe() {
        while(!client.isConnected()){
            System.out.println("aspetto");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(client.isConnected()){
            System.out.println("Quasi ascolto");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Tada");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int qos = 1;
        try {

            client.subscribe(TAKE_PIC, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println("Vogliono vedere");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //EventManager.getInstance().takePicture();
                }
            });



            client.subscribe(SPEECH_TOPIC, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    byte[] payload = message.getPayload();
                    String sss = new String(payload);
                    System.out.println(sss);
                }
            });



            client.subscribe(Topics.RESPONSES.getTopic() + "/" + clientId, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String sss = new String(payload);
                    EventManager.getInstance().speak(sss);

                }
            });


            client.subscribe(MOVE_TOPIC, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try {

                        byte[] payload = message.getPayload();
                        String sss = new String(payload);
                        String[] split = sss.split(":");
                        String direction = split[0];
                        int speed = Integer.parseInt(split[1]);
                        int quantity = Integer.parseInt(split[2]);
                        if (direction.equals(DIRECTION_FORWARD)) {
                            System.out.println("Avanti");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        Toast toast = Toast.makeText(context, errors.toString(), Toast.LENGTH_LONG);
                        toast.setMargin(50, 50);
                        toast.show();
                    }
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("aspeErr nella pubtto");
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast toast=Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG);
            toast.setMargin(50,50);
            toast.show();
        }


    }

    public void imalive(){
        this.publish(IM_ALIVE_TOPIC, "robottino");
    }

    public void publish(String topic, String text){
        try {


            //byte[] encodedPayload = new byte[0];

            //encodedPayload = text.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(text.getBytes());
            client.publish(topic, message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Err pub");
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast toast=Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG);
            toast.setMargin(50,50);
            toast.show();
        }
    }

    public void sendPicture(byte [] image){
        try {


            //byte[] encodedPayload = new byte[0];

            //encodedPayload = text.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(image);
            client.publish(RECEIVE_PIC, message);
            System.out.println("Mandata");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Again err");
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast toast=Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG);
            toast.setMargin(50,50);
            toast.show();
        }
    }

    public void disconnect(){

    }


    public String getId() {
        return this.clientId;
    }
}
