package io.javabrains.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class SearchController {

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping("/search")
    public String getSearch(@RequestParam String query, Model model){
        SearchResult result = webClient.get()
                .uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResult.class)
                .block();
        model.addAttribute("searchResult", result);
        return "search";
    }
}
