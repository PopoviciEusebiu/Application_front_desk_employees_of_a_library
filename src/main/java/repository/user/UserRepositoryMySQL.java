package repository.user;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ? and `password` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);


            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next()) {
                User user = getUserFromResultSet(userResultSet);
                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                 findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                 return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?, ?, null)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setLong(3,1);
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            if (rs.next()) {
                long userId = rs.getLong(1);
                user.setId(userId);

                rightsRolesRepository.addRolesToUser(user, user.getRoles());

                saveNotification.setResult(true);
            } else {
                saveNotification.addError("Failed to retrieve generated keys after user insertion.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Error while saving user to the database");
        }
        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username` = ?";

            PreparedStatement statement = connection.prepareStatement(fetchUserSql);
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        try {
            String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `username` = ?";
            try (PreparedStatement statement = connection.prepareStatement(fetchUserSql)) {
                statement.setString(1, username);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new UserBuilder()
                                .setId(resultSet.getLong("id"))
                                .setUsername(resultSet.getString("username"))
                                .setPassword(resultSet.getString("password"))
                                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                                .build();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User findUserById(Long id) {
        try {
            String fetchUserSql = "SELECT * FROM `" + USER + "` WHERE `id` = ?";
            try (PreparedStatement statement = connection.prepareStatement(fetchUserSql)) {
                statement.setLong(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return getUserFromResultSet(resultSet);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserFromResultSet(ResultSet resultSet) throws SQLException
    {
        return  new UserBuilder()
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .build();
    }

    
}