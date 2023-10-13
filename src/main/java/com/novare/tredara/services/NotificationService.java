package com.novare.tredara.services;

import com.novare.tredara.models.*;
import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.payloads.NotificationDTO;
import com.novare.tredara.repositories.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;

    @Autowired
    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }
    public List<Notification> getNotificationHistory() {
        return notificationRepo.findAll();
    }

    public Notification createNotificationForBidWinner(Bid winningBid) {
        Notification notification = new Notification();
        notification.setStatus(ENotificationStatus.STATUS_PENDING);
        notification.setItem(winningBid.getItem());
        notification.setBid(winningBid);
        notification.setTimestamp(new Date());
        notification.setUser(winningBid.getUser());
        notificationRepo.save(notification);
        return notification;
    }

    public void markNotificationAsSent(Notification notification) {
        notification.setStatus(ENotificationStatus.STATUS_SENT);
        notificationRepo.save(notification);
    }

    public List<NotificationDTO> getFilteredNotificationHistory(NotificationDTO request) {
        return this.getNotificationHistory()
                .stream()
                .map(this::convertToNotificationDTO)
                .filter(dto -> {
                    if (request.getFilterStartInterval() != null && request.getFilterEndInterval() != null) {
                        return dto.getTimestamp().after(request.getFilterStartInterval())
                                && dto.getTimestamp().before(request.getFilterEndInterval());
                    } else if (request.getFilterStartInterval() != null) {
                        return dto.getTimestamp().after(request.getFilterStartInterval());
                    } else if (request.getFilterEndInterval() != null) {
                        return dto.getTimestamp().before(request.getFilterEndInterval());
                    }
                    return true;
                })
                .filter(dto -> {
                    if (request.getStatus() != null) {
                        return dto.getStatus().equalsIgnoreCase(request.getStatus());
                    }
                    return true;
                })
                .filter(dto -> {
                    if (request.getUserId() != null) {
                        return dto.getUserId()==request.getUserId();
                    }
                    return true;
                }).filter(dto -> {
                    if (request.getItemId() != null) {
                        return dto.getItemId()==request.getItemId();
                    }
                    return true;
                }).filter(dto -> {
                    if (request.getBidId() != null) {
                        return dto.getBidId()==request.getBidId();
                    }
                    return true;
                })
                .toList();
    }
    public NotificationDTO convertToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO  = new NotificationDTO();
        notificationDTO.setId(notification.getNotificationId());
        notificationDTO.setStatus(notification.getStatus().name());
        notificationDTO.setTimestamp(notification.getTimestamp());

        if (notification.getUser() != null) {
            notificationDTO.setUserId(notification.getUser().getId());
        }
        if (notification.getItem() != null) {
            notificationDTO.setItemId(notification.getItem().getId());
        }
        if (notification.getBid() != null) {
            notificationDTO.setBidId(notification.getBid().getId());
        }

        return notificationDTO;
    }
}
