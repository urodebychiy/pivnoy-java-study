package ttv.poltoraha.pivka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

// В любом проекте куча пропертей, которая есть в application.yaml. Намного удобнее с этим работать,
// когда у нас эти настройки превращаются в класс.
@Component
@Data
@ConfigurationProperties(prefix = "web")
public class WebServiceProperties {
    private String baseUrl = "dsadas";
    private Duration connectionTimeout;
    private Duration readTimeout;
}
