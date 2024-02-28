package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List <Book> findAll();
    Optional<Book> findById(Long id);
    boolean save(Book book);
    void removeAll();
    Book findByTitle(String title);
    boolean deleteBookByTitle(String title);
    boolean updateStockByTitle(String title, int stock);
    boolean sellBook(String title, User customer, User employee);
    List<Book> findSoldBooks();
    String getBookTitleById(Long id);
}
