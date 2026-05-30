package com.piring.sehat.api.calculator.controller;

import com.piring.sehat.api.auth.dto.ApiResponse;
import com.piring.sehat.api.calculator.dto.CalcHistoryRequest;
import com.piring.sehat.api.calculator.dto.CalcHistoryResponse;
import com.piring.sehat.api.calculator.service.CalcHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PRINSIP SOLID: SRP + DIP
 * =====================================================================
 * Controller ini HANYA mengurus routing HTTP untuk fitur riwayat kalkulator.
 * Semua logika bisnis didelegasikan ke CalcHistoryService (interface).
 *
 * Base URL: /api/calculator/history
 *
 * Endpoint:
 *   POST   /                    → simpan riwayat baru
 *   GET    /                    → ambil semua riwayat (default 30 terbaru)
 *   GET    /?type=bmi&limit=20  → ambil riwayat per jenis kalkulator
 *   DELETE /{id}                → hapus satu entri riwayat
 */
@RestController
@RequestMapping("/api/calculator/history")
public class CalcHistoryController {

    private final CalcHistoryService historyService;

    public CalcHistoryController(CalcHistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Menyimpan satu entri riwayat kalkulator.
     * Dipanggil otomatis setiap user menekan tombol "Hitung" di frontend.
     *
     * POST /api/calculator/history
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CalcHistoryResponse>> saveHistory(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CalcHistoryRequest request) {

        CalcHistoryResponse saved = historyService.saveHistory(jwt, request);
        return ResponseEntity.ok(
                ApiResponse.success("Riwayat berhasil disimpan.", saved));
    }

    /**
     * Mengambil daftar riwayat user.
     * Bisa difilter per jenis kalkulator dengan query param ?type=bmi
     *
     * GET /api/calculator/history?type=bmi&limit=20
     * GET /api/calculator/history?limit=50
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CalcHistoryResponse>>> getHistory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "30") int limit) {

        List<CalcHistoryResponse> history;

        if (type != null && !type.isBlank()) {
            history = historyService.getHistoryByType(jwt, type.toLowerCase(), limit);
        } else {
            history = historyService.getHistory(jwt, limit);
        }

        return ResponseEntity.ok(
                ApiResponse.success("Riwayat berhasil diambil.", history));
    }

    /**
     * Menghapus satu entri riwayat berdasarkan ID.
     *
     * DELETE /api/calculator/history/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHistory(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {

        try {
            historyService.deleteHistory(jwt, id);
            return ResponseEntity.ok(
                    ApiResponse.success("Riwayat berhasil dihapus.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
