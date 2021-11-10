package io.javabrains.home;

import io.javabrains.user.BooksByUser;
import io.javabrains.user.BooksByUserRepository;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final BooksByUserRepository booksByUserRepository;

    public HomeController(BooksByUserRepository booksByUserRepository) {
        this.booksByUserRepository = booksByUserRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model){

        if(principal == null || principal.getAttribute("login") == null)
            return "index";

        String id = principal.getAttribute("login");
        Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(id, CassandraPageRequest.of(0, 100));
        List<BooksByUser> booksByUser = booksSlice.getContent();
        List<BooksByUser> books = booksByUser.stream().distinct().map(book -> {
            String cover = "images/no-image.png";
            if (book.getCoverIds() != null && book.getCoverIds().size() > 0) {
                cover = "https://covers.openlibrary.org/b/id/"
                        + book.getCoverIds().get(0) +
                        "-M.jpg";
            }
            book.setCoverUrl(cover);
            return book;
        }).collect(Collectors.toList());
        model.addAttribute("booksByUser", books);
        return "home";

    }
}
