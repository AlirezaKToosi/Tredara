package com.novare.tredara.services;

import com.novare.tredara.models.*;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.payloads.NotificationDTO;
import com.novare.tredara.repositories.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemDTO> getWonItemsForUser(Long userId) {
        return getNotificationHistory()
                .stream()
                .filter(notification -> notification.getUser() != null && notification.getUser().getId().equals(userId))
                .filter(notification -> notification.getStatus() == ENotificationStatus.STATUS_SENT)
                .map(notification -> convertToItemDTO(notification.getItem()))
                .collect(Collectors.toList());
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
    public ItemDTO convertToItemDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setTitle(item.getTitle());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setStartPrice(item.getStartPrice());
        itemDTO.setStartDateTime(item.getStartDateTime());
        itemDTO.setEndDateTime(item.getEndDateTime());
        itemDTO.setStatus(item.getStatus());
        itemDTO.setUserID(item.getUser().getId());
        itemDTO.setImage_url(item.getImage_url());
        return itemDTO;
    }
}
