package com.piring.sehat.api.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO untuk mengirim data riwayat kalkulator ke frontend.
 *
 * PRINSIP DTO: Memisahkan representasi data internal (Model/Entity)
 * dari data yang dikirim ke klien melalui HTTP.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcHistoryResponse {

    private UUID id;

    /** Jenis kalkulator: "bmi" | "protein" | "genetic" */
    private String calcType;

    /** Data input yang digunakan saat perhitungan */
    private Map<String, Object> inputData;

    /** Hasil perhitungan */
    private Map<String, Object> resultData;

    /** Waktu perhitungan dalam format ISO (HH:mm, dd MMM yyyy) */
    private String createdAt;
}
