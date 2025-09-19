package app.tozzi.manager;

import app.tozzi.domain.*;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDateTime;

import static app.tozzi.domain.ObjectLockConfiguration.*;

@Slf4j
@Service
@Observed(contextualName = "bucketManager")
public class BucketManager {

    @Resource
    private S3Client s3;

    @GlobalTransactional(
            name = "bucket-create-transaction",
            rollbackFor = Exception.class,
            timeoutMills = 90000
    )
    public Bucket createBucket(BucketCreateRequest request) {
        return Bucket.builder()
                .name(request.name())
                .accessType(request.accessType())
                .versioningStatus(Boolean.TRUE.equals(request.versioningEnabled()) ? VersioningStatus.ENABLED : VersioningStatus.SUSPENDED)
                .objectLockConfiguration(request.objectLock() != null
                        ? new ObjectLockConfiguration(true, DefaultRetention.of(request.objectLock().period()))
                        : new ObjectLockConfiguration(false, null)
                )
                .size(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public record BucketCreateRequest(
            String name,
            BucketAccessType accessType,
            Boolean versioningEnabled,
            ObjectLock objectLock
    ) {

        public record ObjectLock(Integer period) {
        }
    }
}
