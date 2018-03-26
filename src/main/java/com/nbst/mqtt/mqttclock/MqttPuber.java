package com.nbst.mqtt.mqttclock;

public class MqttPuber {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stuby
		Mqtt_Pub server = null;
		server = new Mqtt_Pub();
		server.startConnect();
	}

}
