package com.example.BookVault;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// todo: consider renaming to BookServiceTest
class BookVaultApplicationTests {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@BeforeEach
	void setup() {

	}

	@Test
	void contextLoads() {
	}

	@Test
	void canAddBook() {
		// given
		Book book = new Book(123, "Excellent Advice for Living", "Kevin Kelly");

		// when
		Book savedBook = bookService.addBook(book);

		// then
		assertNotNull(savedBook);
		assertEquals("Excellent Advice for Living", savedBook.getTitle());
		assertEquals("Kevin Kelly", savedBook.getAuthor());

	}

	@Test
	void canGetBooks() {
		// given
		Book firstBook = new Book(123, "Tell My Horse", "Zora Neale Hurston");
		Book secondBook = new Book(456, "Excellent Advice for Living", "Kevin Kelly");

		// setup the mock to add books
		when(bookRepository.save(firstBook)).thenReturn(firstBook);
		when(bookRepository.save(secondBook)).thenReturn(secondBook);

		// add books
		bookService.addBook(firstBook);
		bookService.addBook(secondBook);

		// setup the mock to retrieve all the books
		when(bookRepository.findAll()).thenReturn(List.of(firstBook, secondBook));


		// when
		List<Book> retrievedBooks = bookService.getBooks();

		// then
		assertNotNull(retrievedBooks, "The list of books should not be null");
		assertFalse(retrievedBooks.isEmpty(), "The list of books should not be empty");
		assertEquals(2, retrievedBooks.size());

		assertEquals("Tell My Horse", firstBook.getTitle());
	}

	@Test
	void canGetBooksById() {
		// given
		Book sampleBook = new Book(123, "Tell My Horse", "Zora Neale Hurston");

		// setup the mock to save and then find the book
		when(bookRepository.save(sampleBook)).thenReturn(sampleBook);
		when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.of(sampleBook));

		// when
		bookService.addBook(sampleBook);
		Optional<Book> retrievedBook = bookService.getBookById(sampleBook.getId());

		// then
		assertTrue(retrievedBook.isPresent(), "Book should be present");
		assertEquals(sampleBook.getId(), (retrievedBook.get().getId()), "Book ID should match");
		assertEquals(sampleBook.getTitle(), (retrievedBook.get().getTitle()), "Book Title should match");
		assertEquals(sampleBook.getAuthor(), (retrievedBook.get().getAuthor()), "Book Author should match");

	}

	@Test
	void canUpdateBook() {
		// given
		int bookId = 1;
		Book existingBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz");
		Book updatedBook = new Book(bookId, "The Four Agreements", "Don Miguel Ruiz");

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
		when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

		// when
		Book result = bookService.updateBook(bookId, updatedBook);

		// then
		assertNotNull(result);
		assertEquals("The Four Agreements", result.getTitle());
		assertEquals("Don Miguel Ruiz", result.getAuthor());
		verify(bookRepository).save(updatedBook);
	}

	@Test
	void updateBookFailsWhenBookNotFound() {

		// given
		int bookId = 1;
		Book updatedBook = new Book(bookId, "The Three Agreements", "Don Miguel Ruiz");
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

		// when / then
		assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, updatedBook));

	}

	@Test
	void canDeleteBookById() {
		// given
		int bookId = 123;
		when(bookRepository.existsById(bookId)).thenReturn(true);

		// when
		bookService.deleteBookById(bookId);

		// then
		verify(bookRepository, times(1)).deleteById(bookId);
	}

	@Test
	void shouldThrowExceptionWhenBookNotFound() {
		// given
		int bookId = 123;
		when(bookRepository.existsById(bookId)).thenReturn(false);

		// when & then
		assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(bookId));

	}

}
