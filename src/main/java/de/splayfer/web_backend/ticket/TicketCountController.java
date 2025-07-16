package de.splayfer.web_backend.ticket;

import de.splayfer.web_backend.MongoDBDatabase;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket/stats")
public class TicketCountController {

    @GetMapping("/open")
    public ResponseEntity<?> getOpenedTickets() {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        int count = 0;
        for (Document doc : mongoDBDatabase.findAll("ticket"))
            if (!doc.containsKey("supporter"))
                count++;
        return ResponseEntity.ok(count);
    }

    @GetMapping("/claimed")
    public ResponseEntity<?> getClaimedTickets() {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        int count = 0;
        for (Document doc : mongoDBDatabase.findAll("ticket"))
            if (doc.containsKey("supporter"))
                count++;
        return ResponseEntity.ok(count);
    }

    @GetMapping("/archive")
    public ResponseEntity<?> getArchivedTickets() {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        int count = 0;
        for (Document doc : mongoDBDatabase.findAll("ticket-archive"))
            count++;
        return ResponseEntity.ok(count);
    }

    @GetMapping("/closed")
    public ResponseEntity<?> getClosedTickets() {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        int count = 0;
        for (Document doc : mongoDBDatabase.findAll("ticket-closed"))
            count++;
        return ResponseEntity.ok(count);
    }

}
