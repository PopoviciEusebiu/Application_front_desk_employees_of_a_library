package repository.book;

import model.Book;
import model.User;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySql implements BookRepository {

    private final Connection connection;

    public BookRepositoryMySql(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);

            while(resultSet.next())
            {
                books.add(getBookFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return books;
    }


    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book where id=?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getBookFromResultSet(resultSet));
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     *
     * How to reproduce a sql injection attack on insert statement
     *
     *
     * 1) Uncomment the lines below and comment out the PreparedStatement part
     * 2) For the Insert Statement DROP TABLE SQL Injection attack to succeed we will need multi query support to be added to our connection
     * Add to JDBConnectionWrapper the following flag after the DB_URL + schema concatenation: + "?allowMultiQueries=true"
     * 3) book.setAuthor("', '', null); DROP TABLE book; -- "); // this will delete the table book
     * 3*) book.setAuthor("', '', null); SET FOREIGN_KEY_CHECKS = 0; SET GROUP_CONCAT_MAX_LEN=32768; SET @tables = NULL; SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()); SELECT IFNULL(@tables,'dummy') INTO @tables; SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables); PREPARE stmt FROM @tables; EXECUTE stmt; DEALLOCATE PREPARE stmt; SET FOREIGN_KEY_CHECKS = 1; --"); // this will delete all tables. You are not required to know the table name anymore.
     * 4) Run the program. You will get an exception on findAll() method because the test_library.book table does not exist anymore
     */

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book VALUES(null, ? , ? , ?, ?);";

        //String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'"+ book.getTitle()+"\', null );";


        try{

            /*Statement statement = connection.createStatement();
            statement.executeUpdate(newSql);
            return true;*/

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4,book.getStock());

            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted == 1;

        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book;";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Book findByTitle(String title) {
        String sql = "SELECT * FROM book where title=?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return getBookFromResultSet(resultSet);
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteBookByTitle(String title) {
        String sql = "DELETE FROM book WHERE title = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);

            int rowsDeleted = preparedStatement.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateStockByTitle(String title, int newStock) {
        String sql = "UPDATE book SET stock = ? WHERE title = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newStock);
            preparedStatement.setString(2, title);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean sellBook(String title, User customer, User employee) {
        try {
            Statement statement = connection.createStatement();

            String fetchBookIdSql =
                    "SELECT id, stock FROM book WHERE title = '" + title + "'";
            ResultSet resultSet  = statement.executeQuery(fetchBookIdSql);

            int bookId = -1;
            int stock = 0;

            if(resultSet.next()) {
                bookId = resultSet.getInt("id");
                stock = resultSet.getInt("stock");
            }

            if(bookId != -1 && stock > 0) {
                PreparedStatement insertStatement = connection
                        .prepareStatement("INSERT INTO `user_book` VALUES (null, ?, ?, ?)");
                insertStatement.setLong(1, customer.getId());
                insertStatement.setLong(2, bookId);
                insertStatement.setLong(3, employee.getId());
                insertStatement.executeUpdate();

                PreparedStatement updateStatement = connection.prepareStatement("UPDATE book SET stock = stock - 1 WHERE id = ?;");
                updateStatement.setInt(1, bookId);
                updateStatement.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Book> findSoldBooks() {
        String sql = "SELECT book_id FROM user_book;";

        List<Book> soldBooks = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");

                Optional<Book> optionalBook = findById((long) bookId);

                optionalBook.ifPresent(soldBooks::add);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldBooks;
    }

    @Override
    public String getBookTitleById(Long bookId) {
        String sql = "SELECT title FROM book WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("title");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }




    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return  new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}