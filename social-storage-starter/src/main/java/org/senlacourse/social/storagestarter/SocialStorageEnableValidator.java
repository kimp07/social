package org.senlacourse.social.storagestarter;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Log4j
public class SocialStorageEnableValidator implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        StorageProperties configProps
                = Binder.get(environment).bindOrCreate("social-storage", StorageProperties.class);
        if (!uriIsValidAndStorageServiceIsRunning(configProps.getUri())) {
            log.fatal("Storage microservice not defined. please sure that social-storage.uri is valid...");
            throw new RuntimeException();
        }
    }

    private boolean uriIsValidAndStorageServiceIsRunning(String storageUri) {
        if (storageUri == null || storageUri.isEmpty()) {
            return false;
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(storageUri, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (ResourceAccessException e) {
            log.fatal(e.getLocalizedMessage());
            return false;
        }
    }
}
