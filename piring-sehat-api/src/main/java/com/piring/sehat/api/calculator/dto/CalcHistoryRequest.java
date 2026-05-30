package com.piring.sehat.api.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Map;

/**
 * DTO untuk menerima request penyimpanan riwayat kalkulator dari frontend.
 *
 * Kelas ini HANYA bertanggung jawab membawa data request,
 * tidak mengandung logika bisnis apapun.
 */
@Data
public class CalcHistoryRequest {

    /**
     * Jenis kalkulator. Nilai valid: "bmi", "protein", "genetic"
     * Divalidasi dengan regex agar tidak ada nilai sembarangan.
     */
    @NotBlank(message = "calcType tidak boleh kosong")
    @Pattern(
        regexp = "bmi|protein|genetic",
        message = "calcType harus salah satu dari: bmi, protein, genetic"
    )
    private String calcType;

    /** Data input yang dimasukkan user ke form kalkulator */
    @NotNull(message = "inputData tidak boleh null")
    private Map<String, Object> inputData;

    /** Hasil perhitungan yang ditampilkan di UI */
    @NotNull(message = "resultData tidak boleh null")
    private Map<String, Object> resultData;
}
