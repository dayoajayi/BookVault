package com.example.BookVault;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookFeatureTests {

    @Autowired
    private TestRestTemplate mockMvc;

    @Autowired
    private WebTestClient client;


    void addBookAndValidateCreated(TestBook book) {
        addBook(book)
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo(book.title())
                .jsonPath("$.author").isEqualTo(book.author())
                .jsonPath("$.isbn").isEqualTo(book.isbn());
    }

    @Test
    void shouldAddBookWhenBookDoesNotExist() {

        // given
        String isbn = "978-0-59-365453-8";

        // when + then
        addBookAndValidateCreated(new TestBook("Atomic Habits", "James Clear", isbn));

        getBooks()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].title").isEqualTo("Atomic Habits")
                .jsonPath("$[0].author").isEqualTo("James Clear");

    }

    @Test
    void shouldNotAddBookWhenBookAlreadyExists() {

        // given
        TestBook book = new TestBook("Accelerate", "Nicole Forsgren", "978-1-94-278833-1");

        // when
        addBookAndValidateCreated(book);

        // then
        addBook(book).expectStatus().is4xxClientError();
    }


    @Test
    void shouldAllowBorrowingABookThatExistsInTheCatalog() {
        // given
        TestBook book = new TestBook("Domain Storytelling", "Stefan Hofer", "978-3-98-890019-7");
        addBookAndValidateCreated(book);

        // when
        borrowBook(book.isbn()).expectStatus().isOk();

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.borrowed").isEqualTo(true);

    }

    @Test
    void shouldNotAllowBorrowingABookThatExistsInTheCatalogButIsAlreadyCheckedOut() {
        // given
        TestBook book = new TestBook("Test Driven Development: By Example", "Kent Beck", "978-0-32-114653-3");
        addBookAndValidateCreated(book);

        // when
        borrowBook(book.isbn()).expectStatus().isOk();

        // then
        borrowBook(book.isbn()).expectStatus().isBadRequest();
    }

    @Test
    void shouldNotAllowBorrowingABookThatDoesNotExistInTheCatalog() {
        borrowBook("not-registered-isbn").expectStatus().isNotFound();
    }

    ResponseSpec checkBorrowStatus(String isbn) {
        return client.get().uri("/checkout-ledger/{isbn}", isbn)
                .exchange();
    }

    ResponseSpec borrowBook(String isbn) {
        return client.post().uri("/books/{isbn}/checkout", isbn)
                .exchange();
    }

    ResponseSpec addBook(TestBook book) {
        return client.post().uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"title\": \"%s\", \"author\": \"%s\", \"isbn\": \"%s\"}".formatted(book.title(), book.author(), book.isbn()))
                .exchange();
    }

    ResponseSpec getBooks() {
        return client.get().uri("/books")
                .exchange()
                .expectStatus().isOk();
    }


/*
    @Test
    void canAddABook_AndDoesNotDuplicateBook() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book newBook = new Book(bookId, "Atomic Habits", "James Clear", isbn);
        given(bookService.addBook(any(Book.class))).willReturn(newBook);

        // when + then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"%s\", \"title\": \"Atomic Habits\", \"author\": \"James Clear\", \"isbn\": \"1\"}".formatted(uuid)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Atomic Habits"))
                .andExpect(jsonPath("$.author").value("James Clear"));
        verify(bookService).addBook(any(Book.class));


        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"%s\", \"title\": \"Atomic Habits\", \"author\": \"James Clear\", \"isbn\": \"1\"}".formatted(uuid)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.title").value("Atomic Habits"))
                .andExpect(jsonPath("$.author").value("James Clear"));

        verify(bookService).addBook(any(Book.class));
    }

    @Test
    void canGetAllBooks() throws Exception {
        // given
        BookId bookId1 = new BookId();
        Isbn isbn1 = new Isbn("978-0-59-365453-8");
        BookId bookId2 = new BookId();
        Isbn isbn2 = new Isbn("978-0-59-365453-8");
        List<Book> allBooks = List.of(new Book(bookId1, "The Four Agreements", "Don Miguel Ruiz", isbn1),
                            new Book(bookId2, "Excellent Advice for Living", "Kevin Kelly", isbn2));
        when(bookService.getBooks()).thenReturn(allBooks);

        // when + then
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("The Four Agreements"))
                .andExpect(jsonPath("$[1].title").value("Excellent Advice for Living"));

    }

    @Test
    void canGetBookById() throws Exception {
        // given
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book sampleBook = new Book(bookId, "The Four Agreements", "Don Miguel Ruiz", isbn);
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(sampleBook));

        // when + then
        String string = bookId.id().toString();
        mockMvc.perform(get("/books/{id}", string))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Four Agreements"))
                .andExpect(jsonPath("$.author").value("Don Miguel Ruiz"));
    }

    @Test
    void canUpdateBook() throws Exception {
        // given
        BookId bookId = new BookId();
        Isbn isbn = new Isbn("978-0-59-365453-8");
        Book updatedBook = new Book(bookId, "New Title", "New Author", isbn);

        // mock the service layer to return the updated book
        when(bookService.updateBook(eq(bookId), any(Book.class))).thenReturn(updatedBook);

        // when + then
        mockMvc.perform(put("/books/{id}", bookId.id().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Title\", \"author\": \"New Author\", \"isbn\": \"1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author").value("New Author"));

        verify(bookService).updateBook(eq(bookId), any(Book.class));
    }

    @Test
    void canDeleteABook() throws Exception {
        // given
        BookId bookId = new BookId();

        // no need to mock since the service returns void
        doNothing().when(bookService).deleteBookById(bookId);

        // when + then
        mockMvc.perform(delete("/books/{id}", bookId.id().toString()))
                .andExpect(status().isNoContent());

        // verify that the service method was called
        verify(bookService).deleteBookById(bookId);
    }

 */

}


