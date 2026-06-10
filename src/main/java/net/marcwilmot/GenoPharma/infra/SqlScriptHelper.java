package net.marcwilmot.GenoPharma.infra;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SqlScriptHelper {

    private static final String SQL_BASE_PATH = "sql/";
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String getSql(String id) {
        if(id == null || id.isBlank())
            throw new IllegalStateException("SQL id must not be null or blank");
        return cache.computeIfAbsent(id, this::loadSql);
    }

    private String loadSql(String id) {
        String resourcePath = SQL_BASE_PATH + id + ".sql";

        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);

            if(!resource.exists())
                throw new IllegalArgumentException("SQL file not found: classpath: " + resourcePath);
            try(InputStream in = resource.getInputStream()){
                byte[] bytes = in.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8).trim();
            }

        }catch (Exception e) {
            throw new IllegalStateException("Failed to load SQL for id: " + id, e);
        }

    }
}
