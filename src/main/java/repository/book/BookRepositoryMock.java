package repository.book;

import model.Book;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private final List<Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }
    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public void removeAll() {
        books.clear();
    }

    public Book findByTitle(String title) {
        return books.parallelStream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deleteBookByTitle(String title) {
        return books.removeIf(book -> book.getTitle().equals(title));
    }

    @Override
    public boolean updateStockByTitle(String title, int newStock) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                book.setStock(newStock);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean sellBook(String title, User customer, User employee) {
        return false;
    }
    public List<Book> findSoldBooks()
    {
        return null;
    }
    public String getBookTitleById(Long id){ return null;}


}
