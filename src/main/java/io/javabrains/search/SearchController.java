package io.javabrains.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)).build())
                .baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping("/search")
    public String getSearch(@RequestParam String query, Model model){

        SearchResult result = webClient.get()
                .uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResult.class)
                .block();

        List<SearchResultBook> docs = result.getDocs().stream().limit(10).map(book -> {
            book.setKey(book.getKey().replace("/works/", ""));
            String cover = book.getCover_i();
            if(StringUtils.hasText(cover))
                cover = "https://covers.openlibrary.org/b/id/"
                        + cover +
                        "-M.jpg";
            else
                cover = "/images/no-image.png";
            book.setCover_i(cover);
            return book;

        })
                .collect(Collectors.toList());

        docs.forEach(System.out::println);

        model.addAttribute("searchResults", docs);
        return "search";
    }
}
