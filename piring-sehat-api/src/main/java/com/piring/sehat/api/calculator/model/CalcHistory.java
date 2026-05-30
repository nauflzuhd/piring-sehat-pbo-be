package com.piring.sehat.api.calculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #1: ENKAPSULASI
 * =====================================================================
 * Entitas ini merepresentasikan tabel 'calculator_history' di Supabase.
 * Menyimpan riwayat setiap perhitungan kalkulator (BMI, Protein, Genetik).
 *
 * Kolom input_data & result_data bertipe JSONB di PostgreSQL sehingga
 * fleksibel — tiap kalkulator punya skema yang berbeda tanpa perlu
 * membuat tabel terpisah.
 */
@Entity
@Table(name = "calculator_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()")
    private UUID id;

    /** ID pengguna dari auth.users Supabase */
    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    /**
     * Jenis kalkulator: "bmi" | "protein" | "genetic"
     * Enum direpresentasikan sebagai String di DB agar mudah di-query dan ditampilkan.
     */
    @Column(name = "calc_type", nullable = false, length = 20)
    private String calcType;

    /** Data input form (JSON) — disimpan sebagai JSONB di PostgreSQL */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_data", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> inputData;

    /** Hasil perhitungan (JSON) — disimpan sebagai JSONB di PostgreSQL */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result_data", nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> resultData;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamptz DEFAULT now()")
    private Instant createdAt = Instant.now();
}
