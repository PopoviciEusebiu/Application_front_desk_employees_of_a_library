package test;

import database.JDBConnectionWrapper;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepositoryMySql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class JUnitMySql {

    private JDBConnectionWrapper connectionWrapper;
    private BookRepositoryMySql bookRep;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void initialize()
    {
        connectionWrapper = new JDBConnectionWrapper("test_library");
        bookRep = new BookRepositoryMySql(connectionWrapper.getConnection());

        book1 = new BookBuilder().
                setId(1L).
                setTitle("Abc").
                setAuthor("Alin a").
                setPublishedDate(LocalDate.of(1899,10,22)).build();

        book2=new BookBuilder().
                setId(2L).
                setTitle("Xyz").
                setAuthor("Vlad George").
                setPublishedDate(LocalDate.of(1907,2,4)).build();

        book3=new BookBuilder().
                setId(3L).
                setTitle("Abecedar").
                setAuthor("Geo Alex").
                setPublishedDate(LocalDate.of(2011,11,11)).build();

    }

    @Test
    void testSave() {
        assert(bookRep.save(book1));
        assert(bookRep.save(book2));
        assert(bookRep.save(book3));
    }

    @Test
    void testFindById()
    {
        Optional<Book> b = bookRep.findById(32L);
        assertEquals("Optional[ Book author: Geo Alex | title: Abecedar | Published Date: 2011-11-11 ]",b.toString());
    }

    @Test
    void testFindAll()
    {
        List<Book> allBooks = bookRep.findAll();

        assertTrue(allBooks.size() > 0);
    }
}
