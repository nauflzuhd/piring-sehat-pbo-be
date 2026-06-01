package com.piring.sehat.api.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) untuk mengembalikan response data forum ke Frontend.
 * Abstraksi: Menyembunyikan kompleksitas format tanggal bawaan Java (Instant) 
 * menjadi String yang mudah dibaca oleh React.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumThreadResponse {
    private UUID id;
    private UUID authorId;
    private String title;
    private String content;
    private String category;
    private String createdAt; // Dalam format text (contoh: 2026-05-12 10:30)
    private String updatedAt; // Dalam format text
}
