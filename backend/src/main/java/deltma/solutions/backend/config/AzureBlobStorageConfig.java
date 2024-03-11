package deltma.solutions.backend.config;

import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureBlobStorageConfig {

    private final String connectionString = "DefaultEndpointsProtocol=https;AccountName=deltmastorage;AccountKey=AaxkADa6Ep3uCCLQAN4uNV6cHU78rQa0F6loo3otIXO1vx+G/yyw5K+05vrC+1EfvOep/xd6PRnH+AStmpzxnQ==;EndpointSuffix=core.windows.net";

    @Bean
    public BlobServiceClientBuilder blobServiceClientBuilder() {
        return new BlobServiceClientBuilder().connectionString(connectionString);
    }
}
