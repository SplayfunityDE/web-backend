package de.splayfer.web_backend.ticket;

import de.splayfer.web_backend.authentication.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    WebClient webClient = WebClient.builder()
            .baseUrl("https://roonie.splayfer.de/ticket")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + new JwtService().generateBackendToken("web-backend"))
            .build();

    @DeleteMapping("/{id}")
    public void closeTicket(@PathVariable String id, @RequestParam String reason) {
        webClient
                .delete()
                .uri("/" + id + "?reason=" + reason)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @PutMapping("/{id}")
    public void updateTicket(@PathVariable String id, @RequestBody Map<String, String> body) {
        System.out.println(body.toString());
        webClient
                .put()
                .uri("/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
