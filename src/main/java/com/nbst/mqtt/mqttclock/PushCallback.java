package com.nbst.mqtt.mqttclock;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;



/**
 *  mqtt回调类
 * @author zhuyao
 *
 */
public class PushCallback implements MqttCallback {

    private MqttClient client;

    public PushCallback(MqttClient client){
        this.client=client;
    }
	//当连接断丢失,执行该方法进行重连
	@Override
	public void connectionLost(Throwable tw) {
		 System.out.println("Connection lost,Reconnecting......");
        Mqtt_Sub mqtt_sub = new Mqtt_Sub();
        mqtt_sub.startConnect();
    }

	//publish后,会执行该处(接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用)
	@Override
	public void deliveryComplete(IMqttDeliveryToken idt) {
//	    System.out.println("deliveryComplete:" + idt.isComplete() );
	}
	
	// subscribe得到服务器publish的消息会执行该处
	@Override
	public void messageArrived(String topic, MqttMessage mmsg) throws Exception {
		String[] machineCodes=topic.split("/");
		String machineCode=machineCodes[3];
        System.out.println("Delivery Message Topic:" + machineCode);
        System.out.println("Delivery Message Qos:" + mmsg.getQos());
        String payload = new String(mmsg.getPayload());
        System.out.println("Delivery Message Content:" + payload);
        System.out.println("--------------------------------------------------------------------------------------");
           
	}

}
