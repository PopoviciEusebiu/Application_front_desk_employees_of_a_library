package repository.admin;

import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminRepositoryMySQL implements AdminRepository{

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    public AdminRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository, BookRepository bookRepository, UserRepository userRepository){
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification<Boolean> saveEmployee(User employee) {
        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user VALUES (null, ?, ?, null, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, employee.getUsername());
            insertUserStatement.setString(2, employee.getPassword());
            insertUserStatement.setInt(3,1);
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            if (rs.next()) {
                long userId = rs.getLong(1);
                employee.setId(userId);

                rightsRolesRepository.addRolesToUser(employee, employee.getRoles());

                saveNotification.setResult(true);
            } else {
                saveNotification.addError("Failed to retrieve generated keys after user insertion.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Error while saving employee to the database");
        }
        return saveNotification;
    }


    @Override
    public List<User> findAllEmployees()
    {
        String sql = "SELECT * FROM user where `isEmployee` = 1;";

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

    @Override
    public boolean updateEmployee(int id, String newUsername) {
       try
       {
           String updateQuery = "UPDATE user SET `username` = ? WHERE `id` = ? AND `isEmployee` = 1";

           PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
           preparedStatement.setString(1,newUsername);
           preparedStatement.setLong(2,id);

           int rowsUpdated = preparedStatement.executeUpdate();
           return rowsUpdated > 0;

       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
    }

    @Override
    public boolean deleteEmployeeByUsername(String username) {
        String sql = "DELETE FROM user WHERE username = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            int rowsDeleted = preparedStatement.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    @Override
    public Map<User, String> findEmployeeActivity() {
        Map<User, String> activity = new HashMap<>();

        String sql = "SELECT book_id, employee_id " +
                "FROM user_book " +
                "WHERE employee_id IS NOT NULL;";

        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);

            while (resultSet.next()) {
                long bookId = resultSet.getLong("book_id");
                long employeeId = resultSet.getLong("employee_id");

                User employee = userRepository.findUserById(employeeId);
                String bookTitle = bookRepository.getBookTitleById(bookId);

                activity.put(employee, bookTitle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activity;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }
}
