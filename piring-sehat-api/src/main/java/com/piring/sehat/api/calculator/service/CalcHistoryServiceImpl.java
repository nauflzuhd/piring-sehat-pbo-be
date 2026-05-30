package com.piring.sehat.api.calculator.service;

import com.piring.sehat.api.calculator.dto.CalcHistoryRequest;
import com.piring.sehat.api.calculator.dto.CalcHistoryResponse;
import com.piring.sehat.api.calculator.model.CalcHistory;
import com.piring.sehat.api.calculator.repository.CalcHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * PILAR OOP #3: POLIMORFISME (via Implementasi Interface)
 * + PRINSIP SOLID: SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 * =====================================================================
 * Implementasi konkret dari CalcHistoryService.
 * Kelas ini HANYA bertanggung jawab pada logika bisnis riwayat kalkulator.
 */
@Service
public class CalcHistoryServiceImpl implements CalcHistoryService {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")
                             .withZone(ZoneId.systemDefault());

    private final CalcHistoryRepository repository;

    // Dependency Injection via Constructor (Spring Best Practice)
    public CalcHistoryServiceImpl(CalcHistoryRepository repository) {
        this.repository = repository;
    }

    // Implementasi metode dari interface CalcHistoryService
    @Override
    public CalcHistoryResponse saveHistory(Jwt jwt, CalcHistoryRequest request) {
        UUID userId = UUID.fromString(jwt.getSubject());

        CalcHistory entity = new CalcHistory();
        entity.setUserId(userId);
        entity.setCalcType(request.getCalcType());
        entity.setInputData(request.getInputData());
        entity.setResultData(request.getResultData());

        CalcHistory saved = repository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public List<CalcHistoryResponse> getHistory(Jwt jwt, int limit) {
        UUID userId = UUID.fromString(jwt.getSubject());
        int safeLimit = Math.min(Math.max(1, limit), 100); // antara 1-100

        return repository
                .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, safeLimit))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalcHistoryResponse> getHistoryByType(Jwt jwt, String calcType, int limit) {
        UUID userId = UUID.fromString(jwt.getSubject());
        int safeLimit = Math.min(Math.max(1, limit), 100);

        return repository
                .findByUserIdAndCalcTypeOrderByCreatedAtDesc(
                        userId, calcType, PageRequest.of(0, safeLimit))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHistory(Jwt jwt, UUID entryId) {
        UUID userId = UUID.fromString(jwt.getSubject());

        // Memastikan entri yang dihapus benar-benar milik user yang sedang login
        CalcHistory entry = repository.findByIdAndUserId(entryId, userId);
        if (entry == null) {
            throw new IllegalArgumentException(
                    "Riwayat tidak ditemukan atau Anda tidak memiliki akses.");
        }
        repository.delete(entry);
    }

    // ─── Helper: Model → DTO ───
    private CalcHistoryResponse mapToResponse(CalcHistory entity) {
        return new CalcHistoryResponse(
                entity.getId(),
                entity.getCalcType(),
                entity.getInputData(),
                entity.getResultData(),
                DISPLAY_FORMATTER.format(entity.getCreatedAt())
        );
    }
}
