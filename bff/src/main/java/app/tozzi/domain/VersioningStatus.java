package app.tozzi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum VersioningStatus {
    ENABLED,
    SUSPENDED,
    DISABLED,
    ;

    public static VersioningStatus fromString(@Nullable String value) {
        if (value == null) {
            return VersioningStatus.DISABLED;
        }

        for (var status : VersioningStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown versioning status: " + value);
    }

    public String toS3String() {
        if (this == DISABLED) {
            throw new IllegalStateException("Versioning is disabled, cannot convert to S3 string.");
        }
        return StringUtils.capitalize(this.name().toLowerCase(Locale.ROOT));
    }
}
