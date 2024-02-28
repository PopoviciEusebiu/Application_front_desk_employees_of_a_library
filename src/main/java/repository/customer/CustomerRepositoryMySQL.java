package repository.customer;

import model.User;
import model.builder.UserBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class CustomerRepositoryMySQL implements CustomerRepository{

    private final Connection connection;

    public CustomerRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean buyBookByTitle(String title,User user)
    {
        try {
            Statement statement = connection.createStatement();

            String fetchBookIdSql =
                    "SELECT id, stock FROM book WHERE title = '" + title + "'";
            ResultSet resultSet  = statement.executeQuery(fetchBookIdSql);

            int bookId = -1;
            int stock = 0;
            if(resultSet .next())
            {
                bookId = resultSet.getInt("id");
                stock = resultSet.getInt("stock");
            }

            if(bookId != -1 && stock >0)
            {
                PreparedStatement insertStatement = connection
                        .prepareStatement("INSERT INTO `user_book` values (null, ?, ?, null)");
                insertStatement.setLong(1, user.getId());
                insertStatement.setLong(2, bookId);
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
    public boolean sellBookByTitle(String title, User user) {
        try {
            String fetchUserBookSql = "SELECT COUNT(*) AS totalBooksSold FROM user_book WHERE user_id = ? AND book_id IN (SELECT id FROM book WHERE title = ?)";
            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchUserBookSql)) {
                fetchStatement.setLong(1, user.getId());
                fetchStatement.setString(2, title);
                ResultSet resultSet = fetchStatement.executeQuery();

                int totalBooksSold = resultSet.next() ? resultSet.getInt("totalBooksSold") : 0;

                String deleteUserBookSql = "DELETE FROM user_book WHERE user_id = ? AND book_id IN (SELECT id FROM book WHERE title = ?)";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteUserBookSql)) {
                    deleteStatement.setLong(1, user.getId());
                    deleteStatement.setString(2, title);
                    int rowsAffected = deleteStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        String updateStockSql = "UPDATE book SET stock = stock + ? WHERE title = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateStockSql)) {
                            updateStatement.setInt(1, totalBooksSold);
                            updateStatement.setString(2, title);
                            updateStatement.executeUpdate();
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return false;
    }

    @Override
    public List<User> findAllCustomers()
    {
        String sql = "SELECT * FROM user where `isCustomer` = 1;";

        List<User> users = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);

            while(resultSet.next())
            {
                users.add(getUserFromResultSet(resultSet));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }


}
