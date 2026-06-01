package com.piring.sehat.api.forum.service;

import com.piring.sehat.api.forum.dto.ForumThreadRequest;
import com.piring.sehat.api.forum.dto.ForumThreadResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

/**
 * =====================================================================
 * PILAR OOP #4: ABSTRAKSI (ABSTRACTION)
 * =====================================================================
 * Interface ini bertindak sebagai sebuah 'kontrak' (blueprint) untuk fitur forum.
 * Controller hanya perlu mengetahui fungsi-fungsi di sini tanpa perlu pusing
 * memikirkan bagaimana cara fungsi tersebut mengolah data di database.
 */
public interface ForumThreadService {
    
    List<ForumThreadResponse> getAllThreads(String category);
    
    ForumThreadResponse getThreadById(UUID id);
    
    ForumThreadResponse createThread(Jwt jwt, ForumThreadRequest request);
    
    ForumThreadResponse updateThread(Jwt jwt, UUID id, ForumThreadRequest request);
    
    void deleteThread(Jwt jwt, UUID id);
}
