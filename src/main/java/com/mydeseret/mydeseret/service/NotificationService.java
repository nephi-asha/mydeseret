package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.NotificationDto;
import com.mydeseret.mydeseret.model.Notification;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendNotification(User recipient, String message, String link) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setCreatedAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);

        NotificationDto dto = new NotificationDto(
            notification.getId(), 
            notification.getMessage(), 
            notification.isRead(), 
            notification.getCreatedAt(),
            notification.getLink()
        );

        messagingTemplate.convertAndSendToUser(
            recipient.getEmail(), 
            "/queue/notifications", 
            dto
        );
    }

    public List<NotificationDto> getMyNotifications(User user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user)
                .stream()
                .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.isRead(), n.getCreatedAt(), n.getLink()))
                .collect(Collectors.toList());
    }
}