package service.book;

import model.Book;
import model.User;
import repository.book.BookRepository;
import service.book.BookService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }
    @Override
    public Book findByTitle(String title)
    {
        return bookRepository.findByTitle(title);
    }
    @Override
    public boolean deleteBookByTitle(String title)
    {
        return bookRepository.deleteBookByTitle(title);
    }

    @Override
    public boolean updateStockByTitle(String title,int stock)
    {
        return bookRepository.updateStockByTitle(title, stock);
    }
    @Override
    public boolean sellBook(String title, User customer, User employee)
    {
        return bookRepository.sellBook(title, customer, employee);
    }
    @Override
    public List<Book> findSoldBooks()
    {
        return bookRepository.findSoldBooks();
    }
}