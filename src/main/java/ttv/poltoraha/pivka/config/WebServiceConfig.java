package ttv.poltoraha.pivka.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ttv.poltoraha.pivka.properties.WebServiceProperties;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

// почти в каждом проекте есть взаимодействие со смежными сис-ма через HTTP. Снизу пример конфига webClient`a с реактивным стеком
// для общего развития полезно будет почитать про org.springframework.web.reactive.function.client.WebClient
// и в целом что такое реактивщина на основе  Spring WebFlux, на проектах могут быть и обычные веб-клиенты без заёбов

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {
    private final WebServiceProperties webServiceProperties;

    @Bean(name="mainWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(webServiceProperties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(
                        createHttpClient(webServiceProperties.getReadTimeout(), webServiceProperties.getConnectionTimeout()))
                )
                .build();
    }

    private HttpClient createHttpClient(Duration readTimeout, Duration connectionTimeout) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectionTimeout.toMillis())
                .doOnConnected((it) -> {
                    it.addHandlerLast(new ReadTimeoutHandler(readTimeout.toMillis(), TimeUnit.MILLISECONDS));
                    it.addHandlerLast(new WriteTimeoutHandler(readTimeout.toMillis(), TimeUnit.MILLISECONDS));
                });

    }

}
