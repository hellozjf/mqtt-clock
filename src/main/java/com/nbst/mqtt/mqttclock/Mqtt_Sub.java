package com.nbst.mqtt.mqttclock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
 
/**
 * mqtt消息投递subscrible端
 * @author zhuyao
 *
 */
public class Mqtt_Sub {  
	//服务器的地址
    public static final String HOST = "tcp://116.62.67.111:61613";
    private static final String CLIENTID = "subscribe";  
    //订阅主题
    public  String subTopic =  ""; 
    
    private String userName = "admin";  
    private String passWord = "password";  
    private int Qos  = 2;  
    //TOPIC 必须是数组类型
   // private String[] TOPIC = {subTopic};
    //private MqttClient sub_client;  
    public static MqttClient sub_client = null;
    private MqttConnectOptions options;  
    
    //生成接收主题
    public void createSubTopic(){
    	subTopic = "Data/Device/#"; 
    	//TOPIC = {subTopic};
    }
  
    //定时器
    private ScheduledExecutorService scheduler;  
  
    //重新链接机制  
    public void startReconnect() {  
        scheduler = Executors.newSingleThreadScheduledExecutor();  
        scheduler.scheduleAtFixedRate(new Runnable() {  
            public void run() {  
                if (!sub_client.isConnected()) {  
                    try {  
                    	     sub_client.connect(options);  
                    } catch (MqttSecurityException e) {  
                        e.printStackTrace();  
                    } catch (MqttException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }, 0 , 10 * 1000, TimeUnit.MILLISECONDS);  
    }  
  
     //开始链接服务器
    public void startConnect() {
        try {
            String clientid = System.currentTimeMillis() / 100 + "";
            createSubTopic();
            // host为主机名，CLIENTID一般以客户端唯一标识符表示，MemoryPersistence设置CLIENTID的保存形式，默认为以内存保存  
            sub_client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置  
            options = new MqttConnectOptions();  
            // 设置是否清空session,若为true表示每次连接到服务器都以新的身份连接,若为false,服务器保存用户的数据  
            options.setCleanSession(true);  
            // 设置连接的用户名  
            options.setUserName(userName);  
            // 设置连接的密码  
            options.setPassword(passWord.toCharArray());  
            // 设置超时时间 单位为秒  
            options.setConnectionTimeout(10);  
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制  
            options.setKeepAliveInterval(20);  
            // 设置回调  
            sub_client.setCallback(new PushCallback(sub_client));
           //MqttTopic topic = sub_client.getTopic(TOPIC);  
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息    
            // options.setWill(TOPIC, "close".getBytes(), 0, true);  
            sub_client.connect(options);  
            //订阅消息 (Subscribe) 
            sub_client.subscribe(subTopic, Qos);  
            
            
        }
        catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    //断开与服务器的链接
    public void disconnect() {  
         try {  
        	 sub_client.disconnect();  
        }
         catch (MqttException e) {  
            e.printStackTrace();  
        }  
    }  
  
}  