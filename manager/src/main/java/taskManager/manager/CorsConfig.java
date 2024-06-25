package taskManager.manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // Permite todos os domínios. Troque por uma lista específica se necessário.
        config.addAllowedHeader("*"); // Permite todos os cabeçalhos.
        config.addAllowedMethod("*"); // Permite todos os métodos HTTP.
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
