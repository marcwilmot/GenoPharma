package net.marcwilmot.GenoPharma.service.impl;

import net.marcwilmot.GenoPharma.constant.ErrorCode;
import net.marcwilmot.GenoPharma.constant.GenomeReference;
import net.marcwilmot.GenoPharma.constant.SampleStatus;
import net.marcwilmot.GenoPharma.dao.interfaces.SampleDao;
import net.marcwilmot.GenoPharma.dto.SampleDto;
import net.marcwilmot.GenoPharma.service.interfaces.SampleService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class SampleServiceImpl implements SampleService {

    private final SampleDao sampleDao;

    public SampleServiceImpl(SampleDao sampleDao) {
        this.sampleDao = sampleDao;
    }

    @Override
    public GenomeReference inferGenomeReference(Optional<Integer> position) {

        if(position.isEmpty())
            return null;

        switch (position.get()){
            case (249250621) : return GenomeReference.GRCh37;
            case (248956422) : return GenomeReference.GRCh38;
            default: return null;
        }
    }

    @Override
    public void insertSample(SampleDto sampleDto) {
        sampleDao.insertSample(sampleDto);

    }

    @Override
    public void updateSampleStatusAndErrorCode(SampleStatus sampleStatus, Optional<ErrorCode> errorCode, Long sampleId) {

        sampleDao.updateSampleStatusAndErrorCode(sampleStatus, errorCode, sampleId);
    }

    @Override
    public String quickSha256(Path path, Integer size) {

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            try(InputStream in = Files.newInputStream(path);
                DigestInputStream dis = new DigestInputStream(in, md)){

                byte[] buffer = new byte[64 * 1024];
                int remaining = size;
                //while(dis.read(buffer) != -1) {/*No loop DigestInputStream update md*/}

                while(remaining > 0){
                    int toRead = Math.min(buffer.length, remaining);
                    int n  = dis.read(buffer, 0, toRead);
                    if(n == -1) break;
                    remaining -= n;
                }
            }
            return HexFormat.of().formatHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean existsByVcfHash(String sha256) {

        return  sampleDao.existsByVcfHash(sha256);
    }
}
