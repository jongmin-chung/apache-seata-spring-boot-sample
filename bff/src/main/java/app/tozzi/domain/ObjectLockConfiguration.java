package app.tozzi.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

@Builder(toBuilder = true)
public record ObjectLockConfiguration(@NotNull boolean enabled, @Nullable DefaultRetention defaultRetention) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record DefaultRetention(@NotNull ObjectLockMode mode, @NotNull int period) {
        public DefaultRetention {
            if (period < 1 || period > 3650) {
                throw new IllegalArgumentException("보존 기간은 1일 이상 3650일 이하이어야 합니다.");
            }
        }

        public static DefaultRetention of(int period) {
            return new DefaultRetention(ObjectLockMode.GOVERNANCE, period);
        }
    }
}
