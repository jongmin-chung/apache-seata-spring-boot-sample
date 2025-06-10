package app.tozzi.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationPredicate;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.ServerHttpObservationFilter;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Configuration(proxyBeanMethods = false)
public class TracingConfig {

    @Bean
    public ObservationPredicate noRootlessHttpObservations() {
        return (name, context) -> {
            var requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                var observation = (Observation) requestAttributes.getAttribute(ServerHttpObservationFilter.class.getName() + ".observation", SCOPE_REQUEST);
                return (!servletRequestAttributes.getRequest().getRequestURI().startsWith("/actuator")) || (observation == null || !observation.isNoop());
            }

            // The root observation is not stored in the `RequestContextHolder` in this stage and needs to be handled separately
            if (context instanceof ServerRequestObservationContext serverContext) {
                return (!serverContext.getCarrier().getRequestURI().startsWith("/actuator"));
            }

            return true;
        };
    }

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}