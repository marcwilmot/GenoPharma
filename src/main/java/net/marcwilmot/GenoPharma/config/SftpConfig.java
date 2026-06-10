package net.marcwilmot.GenoPharma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.sftp.dsl.Sftp;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
@EnableIntegration
public class SftpConfig {

    //JAKARTA
    @Value("${sftp.host}")
    private String host;
    @Value("${sftp.port}")
    private Integer port;
    @Value("${sftp.user}")
    private String user;
    @Value("${sftp.password}")
    private String password;


    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(false);
        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);
        return factory;
    }

    @Bean
    public MessageChannel sftpStartChannel(){
        return new DirectChannel();
    }

    @MessagingGateway
    public interface SftpGateway{
        @Gateway(requestChannel = "sftpStartChannel")
        void startDownload(String msg);
    }

    @Bean
    public IntegrationFlow stfpFlow(){

        return IntegrationFlow
                .from("sftpStartChannel")
                .handle(Sftp.outboundGateway(sftpSessionFactory(),
                        "mget", "'/upload/genome/vcf/'") //todo hardcode application properties
                        .localDirectory(new File("/home/corv/projects/GenoPharma/in"))
                        .autoCreateLocalDirectory(true)
                        .fileExistsMode(FileExistsMode.REPLACE)
                        .options("-R"))
                .handle(message -> {
                    System.out.println("DESCAGADO " + message.getPayload());
                })
                .get();
    }
}
