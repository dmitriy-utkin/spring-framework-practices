package com.example.spring.fifth.listener;

import com.example.spring.fifth.config.UploadConfig;
import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.service.BookService;
import com.example.spring.fifth.service.CategoryService;
import com.example.spring.fifth.utils.JsonUtil;
import com.example.spring.fifth.utils.model.JsonBookModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.upload", name = "enable", havingValue = "true")
public class AppStartedListener {

    private final UploadConfig uploadConfig;
    private final BookService bookService;
    private final CategoryService categoryService;

    @EventListener(ApplicationStartedEvent.class)
    @Order(1)
    public void uploadBooks() {
        if (!uploadConfig.isEnable() || bookService.count() > 0) {
            return;
        }
        List<JsonBookModel> jsonBooks = JsonUtil.prepareJsonBookList(new File(uploadConfig.getDataPath()));
        List<Book> savedBooks = new ArrayList<>();
        List<String> authors = getAuthors();
        jsonBooks.forEach(b -> {
            Collections.shuffle(authors);
            String author = authors.get(0);
            Category category = categoryService
                    .saveOrGetIfExists(Category.builder().name(b.getCategory().toLowerCase(Locale.ROOT)).build());
            Book book = Book.builder().author(author).category(category).name(b.getName()).build();
            savedBooks.add(bookService.createBook(book));
        });
        int defaultBooksNum = createDefaultBooks(authors);
        log.info("Was saved {} generic books and {} default books", savedBooks.size(), defaultBooksNum);
    }

    private List<String> getAuthors() {
        List<String> authors = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            authors.add("author" + i);
        }
        return authors;
    }

    private int createDefaultBooks(List<String> authors) {
        int bookNumber = uploadConfig.getDefaultBooksNumber();
        Category category = categoryService.saveOrGetIfExists(Category.builder().name("default").build());
        List<Book> savedDefaultBooks = new ArrayList<>();
        for (int i = 1; i <= bookNumber; i++) {
            Collections.shuffle(authors);
            savedDefaultBooks.add(bookService.createBook(Book.builder()
                            .author(authors.get(0))
                            .name("Book_" + i)
                            .category(category)
                    .build()));
        }

        return savedDefaultBooks.size();
    }
}
