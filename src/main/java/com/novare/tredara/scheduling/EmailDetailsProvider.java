package com.novare.tredara.scheduling;

import com.novare.tredara.models.Bid;
import com.novare.tredara.models.Item;
import com.novare.tredara.repositories.BidRepo;
import com.novare.tredara.services.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailDetailsProvider {

    BidRepo bidRepo;

    EmailSenderService emailSenderService;

    public  EmailDetailsProvider(BidRepo bidRepo, EmailSenderService emailSenderService){
        this.bidRepo = bidRepo;
        this.emailSenderService = emailSenderService;
    }

    Logger log = LoggerFactory.getLogger(CustomSchedular.class);

    public void generateEmailDetails(Item item){

        List<Bid> allBids = bidRepo.findByItemId(item.getId());

        String winnerEmail = null;

        List<Bid> uniqueUserBids = allBids.stream().collect(Collectors.toMap(bid -> bid.getUser().getEmail(),
                bid -> bid,
                (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());



        Optional<Bid> highestBid = allBids.stream().max((bid1, bid2) -> Double.compare(bid1.getAmount(), bid2.getAmount()));


        //Sending mail to Winner
        if (highestBid.isPresent()){
            Bid newBid = highestBid.get();

            winnerEmail = newBid.getUser().getEmail();

            log.info("Sending Email to Winner " +  newBid.getUser().getFullName());

            String winnerMailBody = "Congratulations! You are the winner of bid for item " +  item.getTitle() + "." + "\r\n" + "Please visit our site for further processing.";
            String winnerMailSubject = "You win the bid for item " + item.getTitle() + " !";
            emailSenderService.sendSimpleEmail(newBid.getUser().getEmail(),winnerMailBody, winnerMailSubject );
            log.info("Mail sent to winner...");
        }


        //Sending mail to Other Bidders
        for (Bid bid: uniqueUserBids) {

            if (bid.getUser().getEmail()!= winnerEmail) {
                log.info("Sending Email to other participants");
                String participantMailBody = "Thank you for Participating in the Bidding of item " + item.getTitle() +"." + "\r\n" + "Please checkout other items in our site.";
                String participantMailSubject = "Bidding closed for item!";
                emailSenderService.sendSimpleEmail(bid.getUser().getEmail(), participantMailBody, participantMailSubject);
                log.info("Mail sent to participant...");
            }
        }

    }
}
