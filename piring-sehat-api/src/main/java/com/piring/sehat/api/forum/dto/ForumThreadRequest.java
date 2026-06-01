package com.piring.sehat.api.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) untuk menangkap request pembuatan/pembaruan forum.
 * 
 * KENAPA BUTUH DTO? (Pertanyaan Umum Presentasi)
 * - Mencegah "Mass Assignment Vulnerability" (Hacker bisa saja menyisipkan field authorId buatan mereka sendiri).
 * - Menangani Validasi input sebelum mencapai layer service atau database.
 */
@Data
public class ForumThreadRequest {

    @NotBlank(message = "Judul forum tidak boleh kosong")
    @Size(min = 5, max = 255, message = "Judul forum harus di antara 5 hingga 255 karakter")
    private String title;

    @NotBlank(message = "Isi forum tidak boleh kosong")
    @Size(min = 10, message = "Isi forum minimal 10 karakter")
    private String content;

    private String category;
}
