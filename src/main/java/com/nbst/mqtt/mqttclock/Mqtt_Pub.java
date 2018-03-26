package com.nbst.mqtt.mqttclock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mqtt消息投递publish端
 * 
 * @author zhuyao
 * 
 */
@Component
public class Mqtt_Pub {

    @Autowired
    private PushCallbackPub pushCallbackPub;

	@Value("${mqtt.clock.host}")
	private String HOST;

	@Value("${mqtt.clock.topic}")
	private String TOPIC;

	@Value("${mqtt.clock.clientid}")
	private String clientid;

	private MqttClient client = null;
	private MqttConnectOptions options;
	private MqttTopic topic=null;

	@Value("${mqtt.clock.username}")
	private String userName;

	@Value("${mqtt.clock.password}")
	private String passWord;
	private String data = "";

	public MqttMessage message;

	// 重连定时器
	private ScheduledExecutorService scheduler;
	
	// 数据发送定时器
	private ScheduledExecutorService schedulerTimeData;

	// 重新链接机制
	public void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if (!client.isConnected()) {
					try {
						client.connect(options);
						topic = client.getTopic(TOPIC);
						sendData();
						System.out.println("server reconnect success");
						scheduler.shutdown();
					} catch (MqttSecurityException e) {
						e.printStackTrace();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	public void startConnect() {
		try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            // 设置超时时间
            options.setConnectionTimeout(10);
            // 设置会话心跳时间
            options.setKeepAliveInterval(20);

            pushCallbackPub.setClient(client);
            client.setCallback(pushCallbackPub);

			client.connect(options);
			topic = client.getTopic(TOPIC);
			sendData();
		} catch (Exception e) {
			startReconnect();
			e.printStackTrace();
		}
	}

	public void publish(MqttMessage message) throws MqttPersistenceException,
			MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		// System.out.println("message has been send");
		System.out.println("--------------------------");
	}

	// 断开与服务器的链接
	public void disconnect() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	// 发送服务器时间数据
	public void sendData() {
		message = new MqttMessage();
		message.setQos(0);
		schedulerTimeData = Executors.newSingleThreadScheduledExecutor();
		schedulerTimeData.scheduleAtFixedRate(new Runnable() {
			public void run() {
				data="{\"CurTime\":";
				data+=System.currentTimeMillis()/1000+"}";
				System.out.println(data);
				message.setPayload(data.getBytes());
				try {
					publish(message);
				} catch (MqttException e) {
					schedulerTimeData.shutdownNow();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 1 * 1000, TimeUnit.MILLISECONDS);
	}
	
}