package com.piring.sehat.api.forum.repository;

import com.piring.sehat.api.forum.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #2: PEWARISAN (INHERITANCE)
 * =====================================================================
 * Interface ini mewarisi (extends) JpaRepository.
 * Dengan pewarisan ini, ForumThreadRepository otomatis memiliki semua metode dasar 
 * seperti save(), findAll(), findById(), delete() tanpa harus kita tulis kodenya.
 */
@Repository
public interface ForumThreadRepository extends JpaRepository<ForumThread, UUID> {
    
    // Pencarian otomatis berdasarkan kategori dan diurutkan berdasarkan waktu terbaru
    List<ForumThread> findByCategoryOrderByCreatedAtDesc(String category);
    
    // Pencarian semua forum diurutkan dari yang terbaru
    List<ForumThread> findAllByOrderByCreatedAtDesc();
    
    // Mengecek spesifik forum milik author tertentu (berguna untuk update/delete agar aman)
    ForumThread findByIdAndAuthorId(UUID id, UUID authorId);
}
