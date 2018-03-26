package com.nbst.mqtt.mqttclock;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MqttStarter {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stuby

		Mqtt_Sub mqttSub = new Mqtt_Sub();
		mqttSub.startConnect();
		
		Mqtt_Pub server;
		try {
			server = new Mqtt_Pub();
			server.message = new MqttMessage();
			server.message.setQos(1);
			String data = "";
			data = "{\"CurTime\":1494298175,\"DI0\":1,\"DI1\":1,\"DI2\":1,\"DI3\":1,\"DI4\":1,\"DI5\":1,\"Detail\":[{\"Time\":1494298172,\"DI0\":1,\"DI1\":0,\"DI2\":1,\"DI3\":1,\"DI4\":1,\"DI5\":1},{\"Time\":1494298173,\"DI0\":1,\"DI1\":1,\"DI2\":1,\"DI3\":1,\"DI4\":1,\"DI5\":1},{\"Time\":1494298173,\"DI0\":0,\"DI1\":1,\"DI2\":1,\"DI3\":1,\"DI4\":1,\"DI5\":1},{\"Time\":1494298173,\"DI0\":1,\"DI1\":1,\"DI2\":1,\"DI3\":1,\"DI4\":1,\"DI5\":1}]}";
			server.message.setPayload(data.getBytes());
			server.publish(server.message);
			while (true) {
				server.publish(server.message);
				Thread.sleep(1000);
			}
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
