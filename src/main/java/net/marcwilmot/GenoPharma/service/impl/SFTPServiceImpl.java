package net.marcwilmot.GenoPharma.service.impl;

import net.marcwilmot.GenoPharma.config.SftpConfig;
import net.marcwilmot.GenoPharma.service.interfaces.SFTPService;
import org.springframework.stereotype.Service;

@Service
public class SFTPServiceImpl implements SFTPService {
    private final SftpConfig.SftpGateway sftpGateway;

    public SFTPServiceImpl(SftpConfig.SftpGateway sftpGateway) {
        this.sftpGateway = sftpGateway;
    }

    @Override
    public void downloadFiles() {
        sftpGateway.startDownload("start");

    }
}
