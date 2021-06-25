package org.senlacourse.social.storagestarter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("social-storage")
public class StorageProperties {

    private String uri;
    private String endPoint;
}
