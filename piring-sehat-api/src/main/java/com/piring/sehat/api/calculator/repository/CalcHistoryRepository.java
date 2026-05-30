package com.piring.sehat.api.calculator.repository;

import com.piring.sehat.api.calculator.model.CalcHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalcHistoryRepository extends JpaRepository<CalcHistory, UUID> {

    /**
     * Ambil semua riwayat milik user tertentu, terurut dari terbaru.
     * Gunakan Pageable untuk membatasi jumlah data (misal: 50 terakhir).
     */
    List<CalcHistory> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    /**
     * Ambil riwayat milik user berdasarkan jenis kalkulator tertentu.
     * Contoh: ambil 20 riwayat BMI terbaru milik user X.
     */
    List<CalcHistory> findByUserIdAndCalcTypeOrderByCreatedAtDesc(
            UUID userId, String calcType, Pageable pageable);

    /**
     * Cari entri spesifik milik user tertentu (untuk validasi sebelum delete).
     */
    CalcHistory findByIdAndUserId(UUID id, UUID userId);

    /**
     * Hitung jumlah riwayat per user (untuk info statistik atau limit).
     */
    long countByUserId(UUID userId);
}
