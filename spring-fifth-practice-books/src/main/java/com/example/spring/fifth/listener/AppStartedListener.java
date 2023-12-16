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
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
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
        jsonBooks.forEach(b -> {
            Category category = categoryService
                    .saveOrGetIfExists(Category.builder().name(b.getCategory().toLowerCase(Locale.ROOT)).build());
            Book book = Book.builder().category(category).name(b.getName()).build();
            savedBooks.add(bookService.createBook(book));
        });
        int defaultBooksNum = createDefaultBooks();
        log.info("Was saved {} generic books and {} default books", savedBooks.size(), defaultBooksNum);
    }

    private int createDefaultBooks() {
        int bookNumber = uploadConfig.getDefaultBooksNumber();
        Category category = categoryService.saveOrGetIfExists(Category.builder().name("default").build());
        List<Book> savedDefaultBooks = new ArrayList<>();
        for (int i = 1; i <= bookNumber; i++) {
            savedDefaultBooks.add(bookService.createBook(Book.builder()
                            .name("Book_" + i)
                            .category(category)
                    .build()));
        }

        return savedDefaultBooks.size();
    }
}
