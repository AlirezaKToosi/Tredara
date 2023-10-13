package com.novare.tredara.scheduling;

import com.novare.tredara.models.EActionType;
import com.novare.tredara.models.EItemStatus;
import com.novare.tredara.models.Item;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomSchedular {

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private EmailDetailsProvider emailDetailsProvider;
    @Autowired
    private LogService logService;

    Logger log = LoggerFactory.getLogger(CustomSchedular.class);

    @Scheduled(fixedRate = 60000)
    public void getTimeExpiredItemfromDBandSendEmail() {

        log.info("Checking in Database if EndTime of some Item has passed");

        List<Item> items = itemRepo.serchByEndDateTimeAndOpenStatus();

        if(items.size()!=0) {
            for (Item item : items) {
                item.setStatus(EItemStatus.STATUS_CLOSE);
                itemRepo.save(item);
                log.info("Item with item id " + item.getId() + " is Closed." );
                //send mail
                log.info("Sending Email to all bidders and winner!" );
                emailDetailsProvider.generateEmailDetails(item);
                log.info("Successfully sent Email to all bidders and winner" );

            }
        }
        else log.info("No items to process for Notification and mail.");
    }

}
