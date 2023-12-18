package com.example.spring.fifth.web.controller;

import com.example.spring.fifth.mapper.BookMapper;
import com.example.spring.fifth.service.BookService;
import com.example.spring.fifth.web.model.book.BookListResponse;
import com.example.spring.fifth.web.model.book.BookResponse;
import com.example.spring.fifth.web.model.book.UpsertBookRequest;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookMapper bookMapper;

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<BookListResponse> findAll(@RequestBody FindAllSettings findAllSettings) {
        return ResponseEntity.ok(
                bookMapper.bookListToBookListResponse(
                        bookService.findAll(findAllSettings)
                )
        );
    }

    @GetMapping("/{name}")
    public ResponseEntity<BookResponse> findBookByName(@PathVariable String name) {
        return ResponseEntity.ok(
                bookMapper.bookToBookResponse(
                        bookService.findByName(name)
                )
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Long id) {
        return ResponseEntity.ok(
                bookMapper.bookToBookResponse(
                        bookService.findById(id)
                )
        );
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<BookListResponse> findBookByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(
                bookMapper.bookListToBookListResponse(
                        bookService.findAllByAuthor(author)
                )
        );
    }

    @GetMapping("/search/category")
    public ResponseEntity<BookListResponse> searchBookByCategory(@RequestParam("category") String category) {
        return ResponseEntity.ok(
                bookMapper.bookListToBookListResponse(
                        bookService.findAllByCategory(category)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<BookResponse> findBookByBookNameAndAuthor(@RequestParam("name") String name,
                                                                    @RequestParam("author") String author) {
        return ResponseEntity.ok(
                bookMapper.bookToBookResponse(
                        bookService.findByNameAndAuthor(name, author)
                )
        );
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody UpsertBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookMapper.bookToBookResponse(
                        bookService.createBook(
                                bookMapper.requestToBook(request)
                        )
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBookById(@PathVariable Long id, @RequestBody UpsertBookRequest request) {
        return ResponseEntity.ok(
                bookMapper.bookToBookResponse(
                        bookService.updateBook(id, bookMapper.requestToBook(request))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
