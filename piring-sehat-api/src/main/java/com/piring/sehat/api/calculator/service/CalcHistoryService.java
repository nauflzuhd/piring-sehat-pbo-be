package com.piring.sehat.api.calculator.service;

import com.piring.sehat.api.calculator.dto.CalcHistoryRequest;
import com.piring.sehat.api.calculator.dto.CalcHistoryResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #2: ABSTRAKSI
 * + PRINSIP SOLID: DEPENDENCY INVERSION PRINCIPLE (DIP)
 * Interface ini mendefinisikan KONTRAK operasi riwayat kalkulator.
 * Controller hanya mengenal interface ini, bukan implementasinya.
 */
public interface CalcHistoryService {

    /**
     * Simpan satu entri riwayat kalkulator untuk user yang sedang login.
     *
     * @param jwt     Token JWT user yang terautentikasi
     * @param request Data input + hasil perhitungan yang akan disimpan
     * @return Entri riwayat yang baru tersimpan
     */
    CalcHistoryResponse saveHistory(Jwt jwt, CalcHistoryRequest request);

    /**
     * Ambil semua riwayat milik user (50 terbaru secara default).
     *
     * @param jwt  Token JWT user
     * @param limit Jumlah maksimum data yang diambil
     * @return List riwayat terurut dari terbaru
     */
    List<CalcHistoryResponse> getHistory(Jwt jwt, int limit);

    /**
     * Ambil riwayat milik user berdasarkan jenis kalkulator tertentu.
     *
     * @param jwt      Token JWT user
     * @param calcType Jenis kalkulator: "bmi" | "protein" | "genetic"
     * @param limit    Jumlah maksimum data
     * @return List riwayat terfilter
     */
    List<CalcHistoryResponse> getHistoryByType(Jwt jwt, String calcType, int limit);

    /**
     * Hapus satu entri riwayat milik user yang sedang login.
     *
     * @param jwt   Token JWT user
     * @param entryId UUID entri yang ingin dihapus
     */
    void deleteHistory(Jwt jwt, UUID entryId);
}
