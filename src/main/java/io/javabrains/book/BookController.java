package io.javabrains.book;

import io.javabrains.userbooks.UserBooks;
import io.javabrains.userbooks.UserBooksPrimaryKey;
import io.javabrains.userbooks.UserBooksRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final UserBooksRepository userBooksRepository;

    public BookController(BookRepository bookRepository, UserBooksRepository userBooksRepository) {
        this.bookRepository = bookRepository;
        this.userBooksRepository = userBooksRepository;
    }

    @RequestMapping("/books/{bookId}")
    public String showBook(@PathVariable String bookId, @AuthenticationPrincipal OAuth2User principal, Model model){
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            model.addAttribute("book", book);
            String cover = "images/no-image.png";
            if(book.getCoverIds() != null && book.getCoverIds().size() > 0){
                cover = "https://covers.openlibrary.org/b/id/"
                        + book.getCoverIds().get(0) +
                        "-L.jpg";
            }
            model.addAttribute("cover", cover);

            if(principal != null && principal.getAttribute("login") != null) {
                model.addAttribute("loginId", principal.getAttribute("login"));
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(book.getId());
                key.setUserId(principal.getAttribute("login"));
                Optional<UserBooks> userBooksOptional = userBooksRepository.findById(key);

                if(userBooksOptional.isPresent()){
                    UserBooks userBooks = userBooksOptional.get();
                    model.addAttribute("userBooks", userBooks);
                }
                else{
                    model.addAttribute("userBooks", new UserBooks());
                }
            }

            return "book";
        }
        return "book-not-found";
    }
}
