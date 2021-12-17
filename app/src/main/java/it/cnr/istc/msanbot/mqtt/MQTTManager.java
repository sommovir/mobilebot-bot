package it.cnr.istc.msanbot.mqtt;

import android.content.Context;
import android.widget.Toast;

import com.sanbot.opensdk.function.beans.wheelmotion.NoAngleWheelMotion;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.PrintWriter;
import java.io.StringWriter;

import it.cnr.istc.msanbot.RobotManager;
import it.cnr.istc.msanbot.logic.ConnectionEventListener;
import it.cnr.istc.msanbot.logic.DeviceType;
import it.cnr.istc.msanbot.logic.EventManager;
import it.cnr.istc.msanbot.logic.FaceType;
import it.cnr.istc.msanbot.logic.RobotMovement;
import it.cnr.istc.msanbot.logic.Topics;
import it.cnr.istc.msanbot.table.TableModel;

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
    //private static final String TAKE_PIC = "takepic";
    private static final String RECEIVE_PIC = "receive_pic";
    private static String ip;
    private Context context;
    MqttClient client = null;
    private static final Long autoListenBaseDelay = 100L;
    private static final Long autoListenBaseDefault = 500L;


    private MQTTManager() {
        clientId = MqttClient.generateClientId();
        System.out.println("client id = " + clientId);
    }

    public static MQTTManager getInstance() {
        if (_instance == null) {
            _instance = new MQTTManager();
        }
        return _instance;
    }

    public void setIp(String newIp) {
        ip = newIp;
    }

    public void connect(final Context context) {
        synchronized (this) {
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
    }

    public String getAutoListenPhrase(String s) {
        if (!s.contains("ROBOT-TIME")) {
            return s.split(">")[1];
        }
        String[] split = s.split(">");
        return split[2];
    }

    public Long getAutoListenDelay(String s) {
        if (!s.contains("ROBOT-TIME")) {
            System.out.println(autoListenBaseDelay * s.length() + autoListenBaseDefault);
            return autoListenBaseDelay * s.length() + autoListenBaseDefault;
        }
        s = s.substring(s.indexOf(":") + 1, s.length() - 1);
        System.out.println("No if:" + Long.parseLong(s));
        return Long.parseLong(s);
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
        while (!client.isConnected()) {
            System.out.println("aspetto");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (client.isConnected()) {
            System.out.println("Quasi ascolto");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Tada");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int qos = 1;
        try {

            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "img", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println("Vogliono vedere");
                    EventManager.getInstance().showImage(new String(message.getPayload()));
                    //EventManager.getInstance().takePicture();
                }
            });


            client.subscribe(SPEECH_TOPIC, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    byte[] payload = message.getPayload();
                    String sss = new String(payload);
                    System.out.println("SPEECH " + sss);
                }
            });


            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "face", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String face = new String(payload);

                    System.out.println("Faccia:" + face);
                    String[] split = face.split(",");
                    if(split.length == 2){
                        long delay = Long.parseLong(split[1]);
                        RobotManager.getInstance().changeFace(FaceType.of(split[0]),delay);
                    }else{
                        RobotManager.getInstance().changeFace(FaceType.of(face),5000);
                    }


                }
            });

            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "table", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try {
                        System.out.println(topic + "\t" + clientId);

                        byte[] payload = message.getPayload();
                        String tables = new String(payload);
                        System.out.println("---->" + tables);
                        if(tables.contains("<CONTINUE>")){
                            String[] split = tables.split("<CONTINUE>");
                            for(String table: split){
                                TableModel.getInstance().addCurrentTable(table);
                                System.out.println("TABLES: " + table);
                            }
                        }else {
                            System.out.println("TABLES: " + tables);
                            TableModel.getInstance().addCurrentTable(tables);
                        }
                        System.out.println("TABLE LIST SIZE: " + TableModel.getInstance().getCurrentDataTable().size());
                        EventManager.getInstance().showTable();
                    } catch (Exception e){
                        e.printStackTrace();
                    }



                }
            });


            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "link", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String link = new String(payload);
                    System.out.println("ink:" + link);

                    EventManager.getInstance().showLink(link);

                }
            });

            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "img", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String link = new String(payload);

                    System.out.println("img" + link);

                    EventManager.getInstance().showImage(link);

                }
            });

            client.subscribe(Topics.COMMAND.getTopic() + "/" + clientId + "/" + "youtube", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String link = new String(payload);
                    System.out.println("Video" + link);
                    EventManager.getInstance().playYouTubeVideo(link);

                }
            });

            client.subscribe(Topics.ROBOT.getTopic() + "/" + clientId + "/" + "movement", new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String movement = new String(payload);

                    String movementDirection = RobotManager.getInstance().getMovementDirection(movement);
                    String movementVelocityGrade = RobotManager.getInstance().getMovementVelocityTurn(movement);

                    if(movementDirection.equals(NoAngleWheelMotion.ACTION_TURN_LEFT) || movementDirection.equals(NoAngleWheelMotion.ACTION_TURN_RIGHT)){
                        RobotManager.getInstance().turnRobot(RobotMovement.of(movementDirection).toSanbotMovement(),3,new Integer(movementVelocityGrade));
                    }else{
                        RobotManager.getInstance().moveRobot(RobotMovement.of(movementDirection).toSanbotMovement(), sanbotVelocity(movementVelocityGrade), 3);
                    }
                }
            });



            client.subscribe(Topics.RESPONSES.getTopic() + "/" + clientId, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    System.out.println(topic + "\t" + clientId);

                    byte[] payload = message.getPayload();
                    String sss = new String(payload);
                    if (sss.contains("<AUTOLISTEN>")) {
                        System.out.println("Contiene autolisten");
                        try {
                            Long autoListenDelay = getAutoListenDelay(sss);
                            String purePhrase = getAutoListenPhrase(sss);
                            EventManager.getInstance().forceAutoListen(autoListenDelay);
                            sss = purePhrase;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("\t\tFrase:" + sss);
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

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("aspeErr nella pubtto");
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast toast = Toast.makeText(context, errors.toString(), Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();
        }


    }

    public int sanbotVelocity(String x){
        switch (x){
            case "LOW":return 2;
            case "MEDIUM":return 4;
            case "HIGH":return 6;
        }
        return 2;
    }

    public void imalive() {
        this.publish(IM_ALIVE_TOPIC, "robottino");
        publish(Topics.GETDEVICE.getTopic(),clientId+":"+ DeviceType.ROBOT.getDeviceType());
    }

    public void publish(String topic, String text) {
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
            Toast toast = Toast.makeText(context, errors.toString(), Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();
        }
    }

    public void sendPicture(byte[] image) {
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
            Toast toast = Toast.makeText(context, errors.toString(), Toast.LENGTH_LONG);
            toast.setMargin(50, 50);
            toast.show();
        }
    }

    public void disconnect() {
        synchronized (this) {
            if (client == null || !client.isConnected()) {
                return;
            }
            try {
                if (client != null) {
                    client.disconnect();
                    client.close();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    public String getId() {
        return this.clientId;
    }
}
