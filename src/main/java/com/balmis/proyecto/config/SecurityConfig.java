package com.balmis.proyecto.config;

import com.balmis.proyecto.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        System.out.println("CustomUserDetailsService inyectado: " + (customUserDetailsService != null));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/").permitAll()                               // Acceso público a "/"
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()        // Permitir preflight CORS
                    .requestMatchers("/swagger-ui/**",                             
                            "/swagger-ui.html",
                            "/swagger-ui/index.html",
                            "/api-docs/**",
                            "/webjars/swagger-ui/**").permitAll()                   // Acceso público a SWAGGER
                    .requestMatchers("/api/auth/**").permitAll()                  // Acceso público a Identificación
                    .requestMatchers(HttpMethod.GET, "/api/expediciones/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/usuarios/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/security/**").permitAll()
                    .requestMatchers("/api/expediciones/**").hasAnyRole("ADMIN","LOGISTICA","ALMACEN")    // Acceso identificado productos
                    .requestMatchers("/api/usuarios/**").hasRole("ADMIN")                  // Acceso identificado usuarios
                    .requestMatchers("/api/security/**").hasRole("ADMIN")
                    .requestMatchers("/api/estanterias/**").hasRole("ADMIN")
                    .requestMatchers("/api/palets/**").hasRole("ADMIN")
                    .requestMatchers("/api/ubicaciones/**").hasRole("ADMIN")
                        .requestMatchers("/api/pasillos/**").hasRole("ADMIN")
                        .requestMatchers("/api/cajas/**").hasRole("ADMIN")
                        .requestMatchers("/api/terminales/**").hasRole("ADMIN")
                    .requestMatchers("/h2-console/**").hasRole("ADMIN")             // Acceso identificado a consola H2           
                    .anyRequest().denyAll()                                         // Acceso DENEGADO al resto
                )
                .cors(Customizer.withDefaults())
                // Inicio de Configuraciones adicionales para H2
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/h2-console/**") // Desactiva CSRF para H2
                    .disable()
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Crea sesión solo si es necesario (por defecto)
                )
                .headers(headers -> headers
                    .frameOptions(frame -> frame
                        .sameOrigin() // Permite iframes del mismo origen (necesario para H2)
                    )
                )    
                // Fin de Configuraciones adicionales para H2                
                .formLogin(form -> 
                        form.disable()  // desactivamos formulario login por defecto
                )
                // IMPORTANTE:
                // Se desactiva HTTP Basic para evitar el popup nativo del navegador en respuestas 401.
                // El frontend (SPA React) usa autenticación por sesión/cookie (credentials: include)
                // contra /api/auth/login y /api/auth/user.
                // Si se reactiva httpBasic, el navegador puede volver a mostrar la ventana de "Sign in".
                // .httpBasic(httpBasic -> Customizer
                //         .withDefaults()
                // )
                .httpBasic(httpBasic ->
                        httpBasic.disable()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {          // ← muy recomendable añadirlo aquí

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
