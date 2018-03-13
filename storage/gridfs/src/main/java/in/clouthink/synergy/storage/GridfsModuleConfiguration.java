package in.clouthink.synergy.storage;

import in.clouthink.daas.fss.mongodb.MongoModuleConfiguration;
import in.clouthink.synergy.storage.gridfs.GridfsDownloadUrlProvider;
import in.clouthink.synergy.storage.spi.DownloadUrlProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(GridfsConfigureProperties.class)
@Import({MongoModuleConfiguration.class})
public class GridfsModuleConfiguration {

    @Bean
    public DownloadUrlProvider storageService() {
        return new GridfsDownloadUrlProvider();
    }

}
