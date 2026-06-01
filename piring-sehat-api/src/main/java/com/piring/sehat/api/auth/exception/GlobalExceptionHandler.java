package com.piring.sehat.api.auth.exception;

import com.piring.sehat.api.auth.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * POLA DESAIN: GLOBAL EXCEPTION HANDLER (@ControllerAdvice)
 * =====================================================================
 * Kelas ini menangkap seluruh exception yang terjadi di semua Controller
 * secara terpusat, lalu mengubahnya menjadi format respons API yang rapi
 * dan konsisten menggunakan DTO ApiResponse.
 *
 * PRINSIP SOLID YANG DITERAPKAN:
 * - Single Responsibility (SRP): Kelas ini HANYA bertanggung jawab untuk
 *   menangani dan memformat pesan error. Tidak ada logika bisnis di sini.
 * - Open/Closed Principle (OCP): Untuk menambah penanganan error baru,
 *   cukup tambahkan metode @ExceptionHandler baru tanpa mengubah metode
 *   yang sudah ada.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Menangani error saat token JWT tidak valid atau kedaluwarsa.
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            "Token JWT tidak valid atau sudah kedaluwarsa: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Menangani error IllegalArgumentException (input data tidak valid).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            "Parameter tidak valid: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Menangani semua jenis exception umum yang tidak terduga.
     * Berfungsi sebagai "safety net" agar backend tidak pernah mengirimkan
     * stack trace mentah ke klien.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(
            "Terjadi kesalahan internal server: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Menangani error validasi (contoh: teks kurang dari 10 karakter)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }
}
