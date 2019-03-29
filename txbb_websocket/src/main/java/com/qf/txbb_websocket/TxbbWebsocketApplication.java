package com.qf.txbb_websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
public class TxbbWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxbbWebsocketApplication.class, args);
	}

}
