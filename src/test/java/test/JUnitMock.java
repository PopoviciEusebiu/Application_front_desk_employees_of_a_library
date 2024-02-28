package test;

import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryMock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



class JUnitMock {
   private BookRepository bookRep;
   private Book book1;
   private Book book2;
   private Book book3;

    @BeforeEach
    void initialize()
    {
        bookRep = new BookRepositoryMock();

        book1 = new BookBuilder().
                setId(1L).
                setTitle("Cartea Junglei").
                setAuthor("Ion Alexe").
                setPublishedDate(LocalDate.of(1999,10,22)).build();

        book2 = new BookBuilder().
                setId(2L).
                setTitle("Monstrii").
                setAuthor("Alin Ion").
                setPublishedDate(LocalDate.of(2007,5,12)).build();

        book3 = new BookBuilder().
                setId(3L).
                setTitle("Harry Potter").
                setAuthor("Albus Dumbledore").
                setPublishedDate(LocalDate.of(2002,8,26)).build();

        bookRep.save(book1);
        bookRep.save(book2);
        bookRep.save(book3);

    }

    @Test
    void testSave() {
        bookRep.removeAll();
        assert(bookRep.save(book1));
        assert(bookRep.save(book2));
        assert(bookRep.save(book3));
    }

    @Test
    void testFindById()
    {
        Optional<Book> b = bookRep.findById(1L);
        assertEquals("Optional[ Book author: Ion Alexe | title: Cartea Junglei | Published Date: 1999-10-22 ]",b.toString());
    }

    @Test
    void testFindAll()
    {
        List<Book> allBooks = bookRep.findAll();

        assertTrue(allBooks.size() > 0);
    }


}