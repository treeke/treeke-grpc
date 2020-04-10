package com.treeke.grpcclient;

import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.DeliverGrpcServer;
import com.treeke.grpc.grpc.ReportGrpcClient;
import com.treeke.grpcclient.service.InstallChaincodeService;
import com.treeke.grpcclient.service.JoinChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrpcClientApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(GrpcClientApplication.class, args);
	}

	@Autowired
	InstallChaincodeService installChaincodeService;

	@Autowired
	JoinChannelService joinChannelService;

	@Value("${grpc.port}")
	private int port;
	@Value("${grpc.certPath}")
	private String certPath;
	@Value("${grpc.keyPath}")
	private String keyPath;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		DeliverGrpcServer deliverGrpcServer = DeliverGrpcServer.createTLSServer(port, certPath, keyPath);
		deliverGrpcServer.registerInstallChaincode(installChaincodeService);
		deliverGrpcServer.registerJoinChannel(joinChannelService);
		deliverGrpcServer.start();

		ReportGrpcClient liuanyicun = ReportGrpcClient.createTLSClient("liuanyicun", "www.treeke.com", 8882, "C:\\treeke\\ideWorkspace\\treeke-grpc\\grpc-client\\src\\main\\resources\\ca.cer");
	}
}
