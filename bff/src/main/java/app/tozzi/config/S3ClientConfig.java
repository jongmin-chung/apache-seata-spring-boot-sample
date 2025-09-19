package app.tozzi.config;

import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;

@Configuration(proxyBeanMethods = false)
public class S3ClientConfig {

    @Bean
    public S3Client s3Client(S3Properties properties) {
        return S3Client.builder()
                .endpointOverride(URI.create(properties.endpoint()))
                .credentialsProvider(() -> AwsBasicCredentials.create(properties.accessKeyId, properties.secretAccessKey))
                .region(AP_NORTHEAST_2)
                .forcePathStyle(false)
                .build();
    }

    @Validated
    @ConfigurationProperties(prefix = "s3")
    public record S3Properties(
            @Pattern(regexp = "^http://[^/]+$")
            String endpoint,
            String accessKeyId,
            String secretAccessKey
    ) {}

}
