package com.piring.sehat.api.forum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #1: ENKAPSULASI
 * =====================================================================
 * Entitas ini merepresentasikan tabel 'forum_thread' di database Supabase.
 * 
 * Penggunaan anotasi @Data dari Lombok memastikan prinsip enkapsulasi 
 * diterapkan dengan benar secara otomatis, yaitu:
 * - Menyembunyikan data internal (field bersifat private).
 * - Menyediakan akses publik terkontrol (getter & setter).
 * 
 * Dengan ini, variabel internal tidak bisa diubah seenaknya dari luar tanpa
 * melalui metode standar.
 */
@Entity
@Table(name = "forum_thread")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumThread {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    // Menyimpan UUID dari tabel user_profiles / auth.users Supabase
    @Column(name = "author_id", nullable = false, columnDefinition = "uuid")
    private UUID authorId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'general'")
    private String category;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz DEFAULT now()")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz DEFAULT now()")
    private Instant updatedAt = Instant.now();

    // Otomatis memperbarui kolom updated_at setiap kali data di-update (JPA Lifecycle callback)
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
