package io.javabrains.userbooks;

import io.javabrains.book.Book;
import io.javabrains.book.BookRepository;
import io.javabrains.user.BooksByUser;
import io.javabrains.user.BooksByUserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserBooksController {

    private final UserBooksRepository userBooksRepository;
    private final BookRepository bookRepository;
    private final BooksByUserRepository booksByUserRepository;

    public UserBooksController(UserBooksRepository userBooksRepository, BookRepository bookRepository, BooksByUserRepository booksByUserRepository) {
        this.userBooksRepository = userBooksRepository;
        this.bookRepository = bookRepository;
        this.booksByUserRepository = booksByUserRepository;
    }

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,
                                       @AuthenticationPrincipal OAuth2User principal){

        if(principal == null || principal.getAttribute("login") == null)
            return null;

        String bookId = formData.getFirst("bookId");
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(!bookOptional.isPresent())
            return new ModelAndView("redirect:/");
        final Book book = bookOptional.get();

        UserBooks userBooks = new UserBooks();
        String[] startedDate = formData.getFirst("startedDate").split("-");
        String[] finishedDate = formData.getFirst("finishedDate").split("-");
        userBooks.setStartedDate(
                LocalDate.of(Integer.valueOf(startedDate[0]), Integer.valueOf(startedDate[1]), Integer.valueOf(startedDate[2])));
        userBooks.setFinishedDate(
                LocalDate.of(Integer.valueOf(finishedDate[0]), Integer.valueOf(finishedDate[1]), Integer.valueOf(finishedDate[2])));
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooks.setRating(Integer.valueOf(formData.getFirst("rating")));

        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        key.setBookId(formData.getFirst("bookId"));
        key.setUserId(principal.getAttribute("login"));
        userBooks.setPrimaryKey(key);

        UserBooks save = userBooksRepository.save(userBooks);


        System.out.println(formData);

        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setBookId(formData.getFirst("bookId"));
        booksByUser.setId(principal.getAttribute("login"));
        booksByUser.setReadingStatus(userBooks.getReadingStatus());
        booksByUser.setBookName(book.getName());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setRating(userBooks.getRating());
        booksByUserRepository.save(booksByUser);





        return new ModelAndView("redirect:/books/"+formData.getFirst("bookId"));

    }
}
