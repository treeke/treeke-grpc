/*
package com.treeke.grpc;

import io.grpc.netty.NegotiationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TreekeGrpcApplication implements ApplicationRunner{
	@Value("${grpc.port}")
	private int port;
	@Value("${grpc.certPath}")
	private String certPath;
	@Value("${grpc.keyPath}")
	private String keyPath;

	public static void main(String[] args) {
		SpringApplication.run(TreekeGrpcApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		*/
/*GrpcServer server = GrpcServerBuilder.getInstance().forPort(port)
				.serverType(NegotiationType.TLS)
				.addTLSParam(certPath, keyPath)
				.addServers(new HeartBeatServer().bindService())
				.build();
		server.start();*//*

	}
}
*/
