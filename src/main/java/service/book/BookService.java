package service.book;

import model.Book;
import model.User;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    Book findById(Long id);

    boolean save(Book book);

    int getAgeOfBook(Long id);
    boolean deleteBookByTitle(String title);
    boolean updateStockByTitle(String title, int stock);
    Book findByTitle(String title);
    boolean sellBook(String title, User customer, User employee);
    List<Book> findSoldBooks();
}