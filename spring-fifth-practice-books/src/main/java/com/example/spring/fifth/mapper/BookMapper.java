package com.example.spring.fifth.mapper;

import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.web.model.book.BookListResponse;
import com.example.spring.fifth.web.model.book.BookResponse;
import com.example.spring.fifth.web.model.book.UpsertBookRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(BookMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    BookResponse bookToBookResponse(Book book);

    Book requestToBook(UpsertBookRequest request);

    default BookListResponse bookListToBookListResponse(List<Book> books) {
        return BookListResponse.builder()
                .books(books.stream().map(this::bookToBookResponse).toList())
                .build();
    }
}
