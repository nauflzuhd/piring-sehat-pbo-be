package com.piring.sehat.api.forum.controller;

import com.piring.sehat.api.auth.dto.ApiResponse;
import com.piring.sehat.api.forum.dto.ForumThreadRequest;
import com.piring.sehat.api.forum.dto.ForumThreadResponse;
import com.piring.sehat.api.forum.service.ForumThreadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller untuk menangani endpoint terkait Forum Thread.
 * Mengikuti arsitektur MVC, controller berperan sebagai perantara (bridge) 
 * antara request dari luar (View/Frontend) dengan logika internal (Service/Model).
 */
@RestController
@RequestMapping("/api/forums")
public class ForumThreadController {

    private static final Logger logger = LoggerFactory.getLogger(ForumThreadController.class);

    private final ForumThreadService forumService;

    // Dependency Injection
    public ForumThreadController(ForumThreadService forumService) {
        this.forumService = forumService;
    }

    /**
     * Endpoint untuk mendapatkan semua forum thread.
     * Bisa di-filter berdasarkan kategori (opsional).
     * Endpoint ini tidak butuh token JWT (bisa diakses publik), tetapi jika ingin
     * diamankan, bisa disesuaikan di SecurityConfig.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ForumThreadResponse>>> getAllThreads(
            @RequestParam(value = "category", required = false) String category) {
        
        List<ForumThreadResponse> threads = forumService.getAllThreads(category);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data forum", threads));
    }

    /**
     * Endpoint untuk mengambil detail 1 forum berdasarkan ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ForumThreadResponse>> getThreadById(@PathVariable UUID id) {
        try {
            ForumThreadResponse thread = forumService.getThreadById(id);
            return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data forum", thread));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Endpoint untuk membuat forum thread baru (wajib login).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ForumThreadResponse>> createThread(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ForumThreadRequest request) {
        
        try {
            logger.info("Creating forum thread for UID: {}", jwt.getSubject());
            ForumThreadResponse response = forumService.createThread(jwt, request);
            return ResponseEntity.ok(ApiResponse.success("Forum berhasil dibuat", response));
        } catch (Exception e) {
            logger.error("Gagal membuat forum: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("Gagal membuat forum: " + e.getMessage()));
        }
    }

    /**
     * Endpoint untuk mengedit forum thread (hanya pembuat yang bisa).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ForumThreadResponse>> updateThread(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @Valid @RequestBody ForumThreadRequest request) {
        
        try {
            ForumThreadResponse response = forumService.updateThread(jwt, id, request);
            return ResponseEntity.ok(ApiResponse.success("Forum berhasil diperbarui", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Endpoint untuk menghapus forum thread (hanya pembuat yang bisa).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteThread(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        
        try {
            forumService.deleteThread(jwt, id);
            return ResponseEntity.ok(ApiResponse.success("Forum berhasil dihapus", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
