package com.example.BookVault;

import com.example.BookVault.time.TestTimeConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestTimeConfiguration.class)
public class BookFeatureTests {

    @Autowired
    private WebTestClient client;

    @Test
    void shouldAddBookWhenBookDoesNotExist() {
        // given
        reset();
        String isbn = "978-0-59-365453-8";

        // when + then
        addBookAndValidateCreated(new TestBook("Atomic Habits", "James Clear", isbn));

        getBooks()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Atomic Habits")
                .jsonPath("$[0].author").isEqualTo("James Clear");
    }

    @Test
    void shouldNotAddBookWhenBookAlreadyExists() {
        // given
        reset();
        TestBook book = new TestBook("Accelerate", "Nicole Forsgren", "978-1-94-278833-1");

        // when
        addBookAndValidateCreated(book);

        // then
        addBook(book).expectStatus().is4xxClientError();
    }

    @Test
    void shouldAllowBorrowingABookThatExistsInTheCatalog() {
        // given
        reset();
        TestBook book = new TestBook("Domain Storytelling", "Stefan Hofer", "978-3-98-890019-7");
        addBookAndValidateCreated(book);

        // when
        borrowBook(book.isbn()).expectStatus().isOk();

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.checkoutStatus").isEqualTo("CHECKED_OUT");
    }

    @Test
    void shouldSetDueDateWhenBookIsCheckedOut() {
        // given
        reset();
        setDate("2024-11-01");

        TestBook book = new TestBook("Designing Data-Intensive Applications", "Martin Kleppmann", "978-1-44-937332-0");
        addBookAndValidateCreated(book);

        // when
        borrowBook(book.isbn()).expectStatus().isOk();

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.dueDate").isEqualTo("2024-12-01");
    }

    @Test
    void shouldHaveStatusOverdueWhenBookIsCheckedOutPastDueDate() {
        // given
        reset();
        setDate("2024-11-01");
        TestBook book = new TestBook(
                "Growing Object-Oriented Software, Guided by Tests",
                "Steve Freeman, Nat Pryce",
                "978-0-32-150362-6"
        );
        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

        setDate("2024-12-02");

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.checkoutStatus").isEqualTo("OVERDUE");
    }

    @Test
    void shouldNotAllowBorrowingABookThatExistsInTheCatalogButIsAlreadyCheckedOut() {
        // given
        reset();
        TestBook book = new TestBook("Test Driven Development: By Example", "Kent Beck", "978-0-32-114653-3");
        addBookAndValidateCreated(book);

        // when
        borrowBook(book.isbn()).expectStatus().isOk();

        // then
        borrowBook(book.isbn()).expectStatus().isBadRequest();
    }

    @Test
    void shouldNotAllowBorrowingABookThatDoesNotExistInTheCatalog() {
        reset();
        borrowBook("not-registered-isbn").expectStatus().isNotFound();
    }

    @Test
    void shouldNotReturnCheckoutStatusForBookThatDoesNotExistInCatalog() {
        reset();
        checkBorrowStatus("not-registered-isbn").expectStatus().isNotFound();
    }

    @Test
    void shouldAllowCheckedOutBooksToBeReturned() {
        // given
        reset();
        TestBook book = new TestBook("Refactoring", "Martin Fowler", "978-0-13-475759-9");
        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

        // when
        returnBook(book.isbn());

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.checkoutStatus").isEqualTo("AVAILABLE");
    }

    @Test
    void shouldNotAllowAvailableBooksToBeReturned() {
        // given
        reset();
        TestBook book = new TestBook(
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Gamma Erich, Helm Richard, Johnson Ralph, Vlissides John",
                "978-0-32-170069-8"
        );
        addBookAndValidateCreated(book);

        // when
        returnBook(book.isbn())
                .expectStatus().isBadRequest();

        // then
        checkBorrowStatus(book.isbn())
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.checkoutStatus").isEqualTo("AVAILABLE");
    }

    @Test
    void shouldNotAllowReturningABookThatDoesNotExistInTheCatalog() {
        reset();
        returnBook("not-registered-isbn").expectStatus().isNotFound();
    }

    @Test
    void shouldTrackFineForOverdueBooks() {
        // given
        reset();
        setDate("2024-11-01");
        TestBook book = new TestBook("The Pragmatic Programmer", "Andrew Hunt, David Thomas", "978-0-20-161622-4");
        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

        setDate("2024-12-04");

        pause();

        // then
        checkAccountBalance().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(3.0);

        setDate("2024-12-07");

        pause();

        checkAccountBalance().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(6.0);
    }

    @Test
    void shouldAdjustAccountBalanceAccordinglyWhenPaid() {
        // given
        reset();
//        setDate("2024-11-01");
        TestBook book = new TestBook("Clean Code", "Robert C. Martin", "978-0-13-235088-4");
        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

//        setDate("2024-12-04");
        shiftDays(34);

        payAccountBalance(3.0).expectStatus().isOk();

        // then
        checkAccountBalance().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(0.0);
    }

    @Test
    void shouldNotAllowNegativePayment() {
        reset();
        payAccountBalance(-3.0).expectStatus().isBadRequest();
    }

    @Test
    void shouldNotAllowZeroPayment() {
        reset();
        payAccountBalance(0.0).expectStatus().isBadRequest();
    }

    @Test
    void shouldNotAcceptPaymentGreaterThanBalanceDue() {

        reset();

        payAccountBalance(4.0).expectStatus().isBadRequest();
    }

    @SneakyThrows
    @Test
    void shouldNotAllowBorrowingAdditionalBooksWhenBalanceGreaterThanTwenty() {
        reset();

        TestBook book = new TestBook("The Phoenix Project", "Gene Kim, Kevin Behr, George Spafford", "978-1-94-278833-1");

        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

        shiftDays(51);

        pause();

        returnBook(book.isbn()).expectStatus().isOk();

        borrowBook(book.isbn()).expectStatus().isBadRequest();
    }

    @Test
    void shouldAllowBorrowingOnceBalanceIsPaidDownBelowTheThreshold() {
        reset();

        TestBook book = new TestBook("The DevOps Handbook", "Gene Kim, Patrick Debois, John Willis, Jez Humble", "978-1-94-278833-1");
        addBookAndValidateCreated(book);

        borrowBook(book.isbn()).expectStatus().isOk();

        shiftDays(51);

        pause();

        returnBook(book.isbn()).expectStatus().isOk();

        payAccountBalance(2.0).expectStatus().isOk();

        borrowBook(book.isbn()).expectStatus().isOk();
    }

    @Test
    void testingShifts() {
        reset();
        setDate("2024-11-01");

        shiftDays(3);
    }

    ResponseSpec payAccountBalance(Double amount) {
        return client.post().uri("/account/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"amount\": %s}".formatted(amount))
                .exchange();
    }

    ResponseSpec checkAccountBalance() {
        return client.get().uri("/account/balance")
                .exchange();
    }

    void addBookAndValidateCreated(TestBook book) {
        addBook(book)
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo(book.title())
                .jsonPath("$.author").isEqualTo(book.author())
                .jsonPath("$.isbn").isEqualTo(book.isbn());
    }

    ResponseSpec checkBorrowStatus(String isbn) {
        return client.get().uri("/checkout-ledger/{isbn}", isbn)
                .exchange();
    }

    ResponseSpec borrowBook(String isbn) {
        return client.post().uri("/books/{isbn}/checkout", isbn)
                .exchange();
    }

    ResponseSpec returnBook(String isbn) {
        return client.post().uri("/books/{isbn}/return", isbn)
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

    void setDate(String now) {
        client.post().uri("/test-time/{now}", now)
                .exchange()
                .expectStatus().isOk();
    }

    void shiftDays(Integer days) {
        client.post().uri("/shift-time/{days}", days)
                .exchange()
                .expectStatus().isOk();
    }

    void reset() {
        client.post().uri("/reset")
                .exchange()
                .expectStatus().isOk();
    }

    void pause() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
