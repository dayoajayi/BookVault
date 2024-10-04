package com.example.BookVault;

import com.example.BookVault.api.BookController;
import com.example.BookVault.domain.Book;
import com.example.BookVault.domain.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;


    @Test
    void canAddBook() throws Exception {
        // given
        Book newBook = new Book(2, "Atomic Habits", "James Clear");
        given(bookService.addBook(any(Book.class))).willReturn(newBook);

        // when + then
        mockMvc.perform(post("/books")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"id\": 2, \"title\": \"Atomic Habits\", \"author\": \"James Clear\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.title").value("Atomic Habits"))
               .andExpect(jsonPath("$.author").value("James Clear"));

        verify(bookService).addBook(any(Book.class));
    }

    @Test
    void canGetAllBooks() throws Exception {
        // given
        List<Book> allBooks = List.of(new Book(1, "The Four Agreements", "Don Miguel Ruiz"), new Book(2, "Excellent Advice for Living", "Kevin Kelly"));
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
        int bookId = 1;
        Book sampleBook = new Book(bookId, "The Four Agreements", "Don Miguel Ruiz");
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(sampleBook));

        // when + then
        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Four Agreements"))
                .andExpect(jsonPath("$.author").value("Don Miguel Ruiz"));
    }

    @Test
    void canUpdateBook() throws Exception {
        // given
        int bookId = 1;
        Book updatedBook = new Book(bookId, "New Title", "New Author");

        // mock the service layer to return the updated book
        when(bookService.updateBook(eq(bookId), any(Book.class))).thenReturn(updatedBook);

        // when + then
        mockMvc.perform(put("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Title\", \"author\": \"New Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author").value("New Author"));

        verify(bookService).updateBook(eq(bookId), any(Book.class));
    }

    @Test
    void canDeleteABook() throws Exception {
        // given
        int bookId = 1;

        // no need to mock since the service returns void
        doNothing().when(bookService).deleteBookById(bookId);

        // when + then
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());

        // verify that the service method was called
        verify(bookService).deleteBookById(bookId);
    }

}
