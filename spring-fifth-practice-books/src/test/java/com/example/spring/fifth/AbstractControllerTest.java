package com.example.spring.fifth;

import com.example.spring.fifth.mapper.BookMapper;
import com.example.spring.fifth.model.Book;
import com.example.spring.fifth.model.Category;
import com.example.spring.fifth.repository.BookRepository;
import com.example.spring.fifth.service.BookService;
import com.example.spring.fifth.web.model.book.BookFilter;
import com.example.spring.fifth.web.model.book.UpsertBookRequest;
import com.example.spring.fifth.web.model.defaults.FindAllSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import com.redis.testcontainers.RedisContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Sql("classpath:db/init.sql")
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractControllerTest {

    public static final Long UPDATE_ID = 1L;
    public static final FindAllSettings FIND_ALL_SETTINGS = FindAllSettings.builder()
            .pageNum(0)
            .pageSize(20)
            .filter(BookFilter.builder().build())
            .build();
    public static final Book DEFAULT_BOOK1 = Book.builder()
            .id(1L)
            .name("book1")
            .author("author1")
            .category(Category.builder().id(1L).name("default").build())
            .build();
    public static final Book DEFAULT_BOOK2 = Book.builder()
            .id(2L)
            .name("book2")
            .author("author2")
            .category(Category.builder().id(1L).name("default").build())
            .build();

    public static final UpsertBookRequest DEFAULT_CREATE_BOOK = UpsertBookRequest.builder()
            .name("newBook1")
            .author("newAuthor1")
            .category("default1")
            .build();
    public static final UpsertBookRequest DEFAULT_UPDATE_BOOK = UpsertBookRequest.builder()
            .name("updBook1")
            .author("updAuthor1")
            .category("default1")
            .build();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected BookMapper bookMapper;

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected BookService bookService;

    @Autowired
    protected BookRepository bookRepository;

    @RegisterExtension
    protected static WireMockExtension wireMockServer =  WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Container
    protected static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:7.0.12"))
            .withExposedPorts(6379)
            .withReuse(true);

    @Container
    protected static final PostgreSQLContainer POSTGRESQL_CONTAINER = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:12.3-alpine"))
//            .withExposedPorts(5432)
            .withReuse(true);

    @DynamicPropertySource
    public static void registerProperty(DynamicPropertyRegistry registry) {
        String jdbcUrl = POSTGRESQL_CONTAINER.getJdbcUrl();
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        stubClient();
    }

    @AfterEach
    public void afterEach() throws Exception {
        wireMockServer.resetAll();
    }

    private void stubClient() throws Exception {
        List<Book> findAllBooks = new ArrayList<>();
        findAllBooks.add(DEFAULT_BOOK1);
        findAllBooks.add(DEFAULT_BOOK2);
        wireMockServer.stubFor(WireMock.get("/api/book")
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(FIND_ALL_SETTINGS)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findAllBooks))
                        .withStatus(200)));

        Book findByNameBook = DEFAULT_BOOK1;
        wireMockServer.stubFor(WireMock.get("/api/book/" + findByNameBook.getName())
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findByNameBook))
                        .withStatus(200)));

        Book findByIdBook = DEFAULT_BOOK2;
        wireMockServer.stubFor(WireMock.get("/api/book/id/2")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(findByIdBook))));

        UpsertBookRequest createBookRequest = DEFAULT_CREATE_BOOK;
        Book createBookResponse = Book.builder()
                .name(createBookRequest.getName())
                .author(createBookRequest.getAuthor())
                .id(5L)
                .category(Category.builder().name(createBookRequest.getCategory()).id(UPDATE_ID).build())
                .build();
        wireMockServer.stubFor(WireMock.post("/api/book")
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(createBookRequest)))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(201)
                        .withBody(objectMapper.writeValueAsString(bookMapper.bookToBookResponse(createBookResponse)))));

        UpsertBookRequest updateBookRequest = DEFAULT_UPDATE_BOOK;
        Book updateBookResponse = Book.builder()
                .id(UPDATE_ID)
                .name(updateBookRequest.getName())
                .author(updateBookRequest.getAuthor())
                .category(Category.builder().id(1L).name(updateBookRequest.getCategory()).build())
                .build();
        wireMockServer.stubFor(WireMock.put("/api/book/" + updateBookResponse.getId())
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(updateBookRequest)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(bookMapper.bookToBookResponse(updateBookResponse)))));

        wireMockServer.stubFor(WireMock.delete("/api/book/" + UPDATE_ID)
                .willReturn(aResponse()
                        .withStatus(204)));

    }
}
