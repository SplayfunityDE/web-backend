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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ticket/list")
public class TicketListController {

    @GetMapping("/all")
    public ResponseEntity<?> listAllTickets() {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        return ResponseEntity.ok("[" +
                StreamSupport.stream(mongoDBDatabase.findAll("ticket").spliterator(), false)
                .map(doc -> doc.toJson(JsonWriterSettings.builder().indent(true).build()))
                .collect(Collectors.joining(",")) +
                "]");
    }
}
