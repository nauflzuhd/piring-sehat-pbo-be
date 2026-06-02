package com.piring.sehat.api.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig bertindak sebagai pusat konfigurasi keamanan (Security Layer) 
 * untuk mendeklarasikan aturan otorisasi endpoint, aturan CORS, stateless session, 
 * dan aktivasi OAuth2 Resource Server berbasis JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Menggunakan SLF4J Logger standar industri (bukan System.out/err)
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;


    /**
     * Mengkonfigurasi filter keamanan HTTP.
     * Menggunakan pendekatan pemrograman fungsional DSL bawaan Spring Security 6.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Mengaktifkan CORS (Cross-Origin Resource Sharing) menggunakan bean corsConfigurationSource()
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 2. Menonaktifkan perlindungan CSRF (Cross-Site Request Forgery).
            //    Aman dinonaktifkan karena REST API kita stateless (menggunakan Token JWT Bearer, bukan Session Cookie).
            .csrf(AbstractHttpConfigurer::disable)
            
            // 3. Mengatur strategi pembentukan sesi menjadi STATELESS (tanpa menyimpan state user di memori server).
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Mendefinisikan aturan otorisasi rute (Endpoint Security Rules)
            .authorizeHttpRequests(auth -> auth
                // Izinkan preflight request CORS
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // Endpoint di bawah "/api/public/**" bersifat terbuka untuk umum (no auth required)
                .requestMatchers("/api/public/**").permitAll()
                // Izinkan akses publik untuk membaca forum dan balasannya
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/forums", "/api/forums/**").permitAll()
                
                // 👉 PERBAIKAN: Mengizinkan akses publik untuk endpoint pencarian nutrisi makanan
                .requestMatchers("/api/nutrition/**").permitAll()
                
                // Endpoint lainnya WAJIB melalui proses autentikasi (harus mengirimkan JWT valid)
                .anyRequest().authenticated()
            )
            
            // 5. Konfigurasi JWT Resource Server untuk memvalidasi JWT dari Supabase secara otomatis
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );

        return http.build();
    }

    /**
     * Mendefinisikan konfigurasi CORS untuk menerima koneksi dari klien Frontend lokal.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Menerima permintaan dari alamat local development React/Vite mana pun
        configuration.setAllowedOriginPatterns(List.of("*"));
        
        // Mengizinkan metode HTTP yang umum digunakan untuk RESTful API
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Mengizinkan header krusial untuk Authorization Bearer token dan media types
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        
        // Membuka header Authorization agar bisa dibaca oleh frontend
        configuration.setExposedHeaders(List.of("Authorization"));
        
        // Mengizinkan kredensial (seperti Cookies atau auth headers)
        configuration.setAllowCredentials(true);

        // Menerapkan konfigurasi di atas ke seluruh rute (/**)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Bean JwtDecoder kustom yang menggunakan algoritma ES256 (Elliptic Curve)
     * untuk memvalidasi tanda tangan JWT yang diterbitkan oleh Supabase Auth.
     * * Menggunakan Logger SLF4J standar industri untuk pencatatan, bukan System.out.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
                .jwsAlgorithm(SignatureAlgorithm.ES256)
                .build();
        return token -> {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                logger.info("JWT berhasil divalidasi untuk UID: {}", jwt.getSubject());
                return jwt;
            } catch (Exception e) {
                logger.error("Gagal memvalidasi JWT: {}", e.getMessage());
                throw new JwtException("JWT validation failed: " + e.getMessage(), e);
            }
        };
    }
}