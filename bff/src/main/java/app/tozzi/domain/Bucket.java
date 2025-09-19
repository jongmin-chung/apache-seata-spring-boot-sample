package app.tozzi.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static app.tozzi.domain.ObjectLockConfiguration.*;


@Getter
@ToString
@EqualsAndHashCode(of = "name")
public class Bucket {

    private final String name;
    private BucketAccessType accessType;

    private final long size;

    private VersioningStatus versioningStatus;
    private ObjectLockConfiguration objectLockConfiguration;
    private final LocalDateTime createdAt;

    @Builder(toBuilder = true)
    public Bucket(
            String name,
            BucketAccessType accessType,
            long size,
            ObjectLockConfiguration objectLockConfiguration,
            VersioningStatus versioningStatus,
            @Nullable LocalDateTime createdAt)
    {
        if (objectLockConfiguration.enabled() && versioningStatus != VersioningStatus.ENABLED) {
            throw new IllegalArgumentException("객체 잠금을 활성화하려면 버전 관리 활성화 요함");
        }
        this.name = name;
        this.accessType = accessType;
        this.versioningStatus = versioningStatus;
        this.objectLockConfiguration = objectLockConfiguration;
        this.size = size;
        this.createdAt = createdAt == null ? LocalDateTime.now(ZoneId.systemDefault()) : createdAt;
    }

    /// 버킷 단위의 객체 잠금 설정과 객체 잠금 보존 기간은 동시에 설정되어야 합니다.
    public void updateObjectLockRetention(DefaultRetention defaultRetention) {
        if (!objectLockConfiguration.enabled()) {
            throw new IllegalStateException("객체 잠금 변경은 객체 잠금 활성화 상태에서만 가능 (객체 생성시만 설정 가능)");
        }

        if (this.versioningStatus != VersioningStatus.ENABLED) {
            throw new IllegalStateException("객체 잠금을 활성화하려면 버전 관리가 활성화 요함");
        }

        this.objectLockConfiguration = new ObjectLockConfiguration(true, defaultRetention);
    }

    public boolean hasObject() {
        return size > 0;
    }

    public void updateVersioningStatus(VersioningStatus versioningStatus) {
        if (this.versioningStatus != VersioningStatus.DISABLED && versioningStatus == VersioningStatus.DISABLED) {
            throw new IllegalStateException("버전 관리 활성화 시 비활성화로 변경할 수 없음");
        }
        if (this.versioningStatus == VersioningStatus.DISABLED && versioningStatus == VersioningStatus.SUSPENDED) {
            throw new IllegalStateException("버전 관리 비활성화 상태에서 일시 중지로 변경할 수 없음");
        }
        if (objectLockConfiguration.enabled() && versioningStatus == VersioningStatus.DISABLED) {
            throw new IllegalStateException("객체 잠금 활성화 시 버전 관리를 비활성화할 수 없음");
        }

        this.versioningStatus = versioningStatus;
    }

    public void updateAccessType(BucketAccessType accessType) {
        this.accessType = accessType;
    }
}
