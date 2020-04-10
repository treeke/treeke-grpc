package com.treeke.grpcserver;

import com.cetcxl.luna.protos.thrid.DeliverGrpc;
import com.treeke.grpc.cert.CertManager;
import com.treeke.grpc.grpc.DeliverGrpcClient;
import com.treeke.grpc.grpc.ReportGrpcServer;
import com.treeke.grpc.model.JoinChannelRep;
import com.treeke.grpc.model.JoinChannelReq;
import com.treeke.grpcserver.service.GrpcService;
import com.treeke.grpcserver.service.InstallChaincodeService;
import com.treeke.grpcserver.service.JoinChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrpcServerApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}

	@Autowired
	InstallChaincodeService installChaincodeService;

	@Autowired
	JoinChannelService joinChannelService;

	@Autowired
	GrpcService grpcService;

	@Value("${grpc.port}")
	private int port;
	@Value("${grpc.certPath}")
	private String certPath;
	@Value("${grpc.keyPath}")
	private String keyPath;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ReportGrpcServer reportGrpcServer = ReportGrpcServer.createTLSServer(port, certPath, keyPath);
		reportGrpcServer.registerInstallReport(installChaincodeService);
		reportGrpcServer.registerJoinReport(joinChannelService);
		reportGrpcServer.start();

		/*DeliverGrpcClient treeke = DeliverGrpcClient.createTLSClient("treeke", "www.liuanyicun.com", 8881, "C:\\treeke\\ideWorkspace\\treeke-grpc\\grpc-server\\src\\main\\resources\\server.cer");
		DeliverGrpc.DeliverBlockingStub deliverBlockingStub = treeke.getDeliverBlockingStub();*/

		String s = "-----BEGIN CERTIFICATE-----\n" +
				"MIIDYzCCAksCFDjx/TCj3s7lYGoxeSxmz5lOASVsMA0GCSqGSIb3DQEBBQUAMG4x\n" +
				"CzAJBgNVBAYTAkNOMQ8wDQYDVQQIDAZ0cmVla2UxDzANBgNVBAcMBnRyZWVrZTEP\n" +
				"MA0GA1UECgwGdHJlZWtlMQ8wDQYDVQQLDAZ0cmVla2UxGzAZBgNVBAMMEnd3dy5s\n" +
				"aXVhbnlpY3VuLmNvbTAeFw0yMDA0MDMwOTM3MjhaFw0zMDA0MDEwOTM3MjhaMG4x\n" +
				"CzAJBgNVBAYTAkNOMQ8wDQYDVQQIDAZ0cmVla2UxDzANBgNVBAcMBnRyZWVrZTEP\n" +
				"MA0GA1UECgwGdHJlZWtlMQ8wDQYDVQQLDAZ0cmVla2UxGzAZBgNVBAMMEnd3dy5s\n" +
				"aXVhbnlpY3VuLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKcD\n" +
				"o+WAZ50d2IJqGZn/1ky9e7tf7R73trle5toBoClwwUvatMQwi3Jd7y/RfGffKRxd\n" +
				"HI3FJvMkxGcfCXhwAKZjaExsfEVYah0hqD2akaKnI/bJfE1NepwER4vK33//Ncot\n" +
				"NbMXqTac1XMtT5KT+uaB0lu1nt+gV88v3WBs3cREDWjOqLObFGomafgNZ3v4IEcP\n" +
				"qKkWb3SXTcyfzq3L733lWQPVpOCbPjS1x71uRP4Z5sQh1BkL0qF4QfVAOAbt5ldr\n" +
				"2sEWRARPqs7EYDfDH+UiEa8/C16Gw9+UEjs+oW8d/dPyO+yv3MCVGR8L9JnBMamc\n" +
				"I1X2SyJcHn4IXOexgIECAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAnzTz3xTLtRDN\n" +
				"zcT9+jn4TAByXPfAqXohjPe/tyA8Gs4seQAXL+SvTZSBVQrO49erEGvsatNc4f82\n" +
				"joD2vyYjChATRLMfL5BOUkdRlY1sQkG1oOcfrM+fMS22bSWS/4OfxUlePhy8W8QY\n" +
				"f3I/Gz3m0qhC7FdtTHM8Zk9J8z2VUOhuOC3AVAofCUDFIriLLb93GL7zGp628zv6\n" +
				"aRybT3RqpEHcNJLTLDiH58cBLn8r91cQwuT1R1Lx9FCmwm3Rf1UcdDi50sdNJhA8\n" +
				"Z6WyTbzUSQN2fiQn7y2ID6tjvF+69WcZ+3eSbWbtCCn1SvEta9HQ9HItahbkJFbj\n" +
				"UXgDGYR8Rg==\n" +
				"-----END CERTIFICATE-----\n";

		//JoinChannelRep joinChannelRep = new JoinChannelRep("1","325100018718696290646616535087386869891390842220","561508602909572534598573892942326516584522200757",new byte[]{11,22,33,44,55});
		JoinChannelReq joinChannelReq = JoinChannelReq.builder()
				.host("www.liuanyicun.com").port(8881)
				.srcCertSerialNumber(CertManager.getCertSerialNumber())
				.destCertSerialNumber("325100018718696290646616535087386869891390842220")
				.tlsCert(s.getBytes())
				.privateKey("")
				.id("1").block(new byte[]{11,22,33,44,55}).build();
		grpcService.joinChannel(joinChannelReq);
	}
}
