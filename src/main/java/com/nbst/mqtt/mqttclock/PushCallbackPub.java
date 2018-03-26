package com.nbst.mqtt.mqttclock;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *  mqtt回调类
 * @author zhuyao
 *
 */
@Component
public class PushCallbackPub implements MqttCallback {

    private MqttClient client;

    private Mqtt_Pub mqtt_pub;

    @Autowired
	public void setMqtt_pub(Mqtt_Pub mqtt_pub) {
		this.mqtt_pub = mqtt_pub;
	}

	//    public PushCallbackPub(MqttClient client){
//        this.client=client;
//    }

	public void setClient(MqttClient client) {
		this.client = client;
	}

	//当连接断丢失,执行该方法进行重连
	@Override
	public void connectionLost(Throwable tw) {
		 System.out.println("Connection lost,Reconnecting......");
		 try {
			client.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 client=null;
		 mqtt_pub.startConnect();
    }

	//publish后,会执行该处(接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用)
	@Override
	public void deliveryComplete(IMqttDeliveryToken idt) {
//	    System.out.println("deliveryComplete:" + idt.isComplete() );
	}
	
	// subscribe得到服务器publish的消息会执行该处
	@Override
	public void messageArrived(String topic, MqttMessage mmsg) throws Exception {
//		STRING[] MACHINECODES=TOPIC.SPLIT("/");
//		STRING MACHINECODE=MACHINECODES[3];
//        SYSTEM.OUT.PRINTLN("DELIVERY MESSAGE TOPIC:" + MACHINECODE);
//        SYSTEM.OUT.PRINTLN("DELIVERY MESSAGE QOS:" + MMSG.GETQOS());
//        STRING PAYLOAD = NEW STRING(MMSG.GETPAYLOAD());
//        SYSTEM.OUT.PRINTLN("DELIVERY MESSAGE CONTENT:" + PAYLOAD);
//        SYSTEM.OUT.PRINTLN("--------------------------------------------------------------------------------------");
	}

}
