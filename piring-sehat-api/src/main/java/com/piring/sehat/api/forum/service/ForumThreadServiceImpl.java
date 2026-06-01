package com.piring.sehat.api.forum.service;

import com.piring.sehat.api.forum.dto.ForumThreadRequest;
import com.piring.sehat.api.forum.dto.ForumThreadResponse;
import com.piring.sehat.api.forum.model.ForumThread;
import com.piring.sehat.api.forum.repository.ForumThreadRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * =====================================================================
 * PILAR OOP #3: POLIMORFISME (POLYMORPHISM)
 * =====================================================================
 * Kelas ini adalah WUJUD NYATA (Implementasi) dari interface ForumThreadService.
 * Berkat polimorfisme, controller bisa memanggil berbagai metode di bawah ini 
 * hanya dengan menggunakan tipe datanya (Interface), tanpa peduli logika aslinya.
 */
@Service
public class ForumThreadServiceImpl implements ForumThreadService {

    private final ForumThreadRepository repository;

    // Dependency Injection (mencegah instance manual dengan keyword 'new')
    public ForumThreadServiceImpl(ForumThreadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ForumThreadResponse> getAllThreads(String category) {
        List<ForumThread> threads;
        if (category != null && !category.trim().isEmpty()) {
            threads = repository.findByCategoryOrderByCreatedAtDesc(category);
        } else {
            threads = repository.findAllByOrderByCreatedAtDesc();
        }
        
        return threads.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ForumThreadResponse getThreadById(UUID id) {
        ForumThread thread = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Forum tidak ditemukan"));
        return mapToResponse(thread);
    }

    @Override
    public ForumThreadResponse createThread(Jwt jwt, ForumThreadRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject()); // Ekstrak UUID user dari token Supabase

        ForumThread thread = new ForumThread();
        thread.setId(UUID.randomUUID()); // Generate UUID secara manual
        thread.setAuthorId(authorId);
        thread.setTitle(request.getTitle());
        thread.setContent(request.getContent());
        thread.setCategory(request.getCategory() != null ? request.getCategory() : "general");
        
        ForumThread savedThread = repository.save(thread);
        return mapToResponse(savedThread);
    }

    @Override
    public ForumThreadResponse updateThread(Jwt jwt, UUID id, ForumThreadRequest request) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        
        // Memastikan forum tersebut memang milik author yang sedang login
        ForumThread existingThread = repository.findByIdAndAuthorId(id, authorId);
        if (existingThread == null) {
            throw new IllegalArgumentException("Forum tidak ditemukan atau Anda tidak memiliki akses untuk mengubahnya.");
        }
        
        existingThread.setTitle(request.getTitle());
        existingThread.setContent(request.getContent());
        if (request.getCategory() != null) {
            existingThread.setCategory(request.getCategory());
        }
        
        ForumThread updatedThread = repository.save(existingThread);
        return mapToResponse(updatedThread);
    }

    @Override
    public void deleteThread(Jwt jwt, UUID id) {
        UUID authorId = UUID.fromString(jwt.getSubject());
        
        ForumThread existingThread = repository.findByIdAndAuthorId(id, authorId);
        if (existingThread != null) {
            repository.delete(existingThread);
        } else {
            throw new IllegalArgumentException("Forum tidak ditemukan atau Anda tidak memiliki akses untuk menghapusnya.");
        }
    }

    // Helper method untuk memetakan Model -> DTO
    private ForumThreadResponse mapToResponse(ForumThread thread) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        .withZone(ZoneId.systemDefault());
                                        
        return new ForumThreadResponse(
                thread.getId(),
                thread.getAuthorId(),
                thread.getTitle(),
                thread.getContent(),
                thread.getCategory(),
                formatter.format(thread.getCreatedAt()),
                formatter.format(thread.getUpdatedAt())
        );
    }
}
