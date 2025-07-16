package de.splayfer.web_backend.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.splayfer.web_backend.MongoDBDatabase;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/ticket/chart")
public class ChartController {

    @GetMapping("/topics")
    public ResponseEntity<?> getTopicChartData() throws JsonProcessingException {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        HashMap<Integer, Integer> topicMap = new HashMap<>();
        for (Document doc : mongoDBDatabase.findAll("ticket")) {
            int topic = doc.getInteger("type");
            topicMap.put(topic, topicMap.getOrDefault(topic, 0) + 1);
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(topicMap));
    }

}
