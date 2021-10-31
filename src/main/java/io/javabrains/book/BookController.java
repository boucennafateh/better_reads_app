package io.javabrains.book;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping("/users/{bookId}")
    public String showBook(@PathVariable String bookId, Model model){
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            model.addAttribute("book", book);
            String cover = "/images/no-image.png";
            if(book.getCoverIds() != null && book.getCoverIds().size() > 0){
                cover = "https://covers.openlibrary.org/b/id/"
                        + book.getCoverIds().get(0) +
                        "-L.jpg";
            }
            model.addAttribute("cover", cover);
            return "book";
        }
        return "book-not-found";
    }
}
