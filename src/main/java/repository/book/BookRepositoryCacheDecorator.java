package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator implements BookRepository {

    private final BookRepository decoratedRepository;
    private final Cache cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache cache) {
        this.decoratedRepository = bookRepository;
        this.cache = cache;
    }

    @Override
    public List findAll() {
        if (cache.hasResult()) {
            return cache.load();
        }

        List books = decoratedRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Optional findById(Long id) {
        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }

    @Override
    public Book findByTitle(String title) {
        return decoratedRepository.findByTitle(title);
    }

    @Override
    public boolean deleteBookByTitle(String title){
        return decoratedRepository.deleteBookByTitle(title);
    }

    @Override
    public boolean updateStockByTitle(String title, int stock)
    {
        return decoratedRepository.updateStockByTitle(title, stock);
    }
    public boolean sellBook(String title, User customer, User employee)
    {
        return decoratedRepository.sellBook(title, customer, employee);
    }
    public List<Book> findSoldBooks(){
        return decoratedRepository.findSoldBooks();
    }
    public String getBookTitleById(Long id){ return decoratedRepository.getBookTitleById(id);}
}
