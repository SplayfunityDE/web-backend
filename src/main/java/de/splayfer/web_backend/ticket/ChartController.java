package de.splayfer.web_backend.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.splayfer.web_backend.MongoDBDatabase;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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

    @GetMapping("/status")
    public ResponseEntity<?> getStatusChartData() throws JsonProcessingException {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        HashMap<String, Integer> statusMap = new HashMap<>();
        for (Document doc : mongoDBDatabase.findAll("ticket")) {
            if (doc.containsKey("supporter"))
                statusMap.put("bearbeitung", statusMap.getOrDefault("bearbeitung", 0) + 1);
            else
                statusMap.put("offen", statusMap.getOrDefault("offen", 0) + 1);
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(statusMap));
    }

    @GetMapping("/activity")
    public ResponseEntity<?> getActivityChartData() throws JsonProcessingException {
        MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("splayfunity");
        HashMap<String, Integer> activityMap = new HashMap<>();
        LocalDateTime currentDate = LocalDateTime.now();
        for (Document doc : mongoDBDatabase.findAll("ticket")) {
            LocalDateTime date = LocalDateTime.ofInstant(doc.getDate("createDate").toInstant(), ZoneId.systemDefault());
            if (date.isAfter(currentDate.minusDays(1))) //tickets created in the last 24 hours mit not be included in minusDay(1)
                activityMap.put(String.valueOf(0), activityMap.getOrDefault(String.valueOf(0), 0) + 1);
            for (int i = 0; i < 6; i++) {
                if (date.isBefore(currentDate.minusDays(7 * i)) && date.isAfter(currentDate.minusDays(7 * (i + 1)))) {
                    activityMap.put(String.valueOf(i), activityMap.getOrDefault(String.valueOf(i), 0) + 1);
                    break;
                }
            }
        }
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(activityMap));
    }

}
