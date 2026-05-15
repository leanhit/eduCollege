package com.educollege.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Notification Service for real-time alerts
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Send a notification to a specific user
     */
    public void sendNotification(String username, String message) {
        log.info("Sending notification to user {}: {}", username, message);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }

    /**
     * Send a broadcast notification to all users
     */
    public void broadcastNotification(String message) {
        log.info("Broadcasting notification: {}", message);
        messagingTemplate.convertAndSend("/topic/public-notifications", message);
    }

    /**
     * Notify student about a new grade
     */
    public void notifyGradeSubmission(String username, String courseName, Double grade) {
        String message = String.format("Kết quả học tập mới: Môn %s đã có điểm. Điểm của bạn là: %.1f", courseName, grade);
        sendNotification(username, message);
    }
}
