package de.splayfer.web_backend.ticket;

import de.splayfer.web_backend.MongoDBDatabase;
import org.bson.json.JsonWriterSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/ticket/list")
public class TicketListController {

    @GetMapping("/all")
    public ResponseEntity<?> listAllTickets() {
        return getCollectionAsResponseEntity("ticket");
    }

    @GetMapping("/closed")
    public  ResponseEntity<?> listClosedTickets() {
        return getCollectionAsResponseEntity("ticket-closed");
    }

    @GetMapping("/archive")
    public ResponseEntity<?> listArchivedTickets() {
        return getCollectionAsResponseEntity("ticket-archive");
    }

    public ResponseEntity<?> getCollectionAsResponseEntity(String collection) {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        return ResponseEntity.ok("[" +
                StreamSupport.stream(mongoDBDatabase.findAll(collection).spliterator(), false)
                        .map(doc -> doc.toJson(JsonWriterSettings.builder().indent(true).build()))
                        .collect(Collectors.joining(",")) +
                "]");
    }
}
