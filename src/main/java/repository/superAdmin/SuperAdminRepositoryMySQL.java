package repository.superAdmin;

import model.User;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SuperAdminRepositoryMySQL implements SuperAdminRepository {
    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public SuperAdminRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public void saveAdmin(User admin) {
        try {
            PreparedStatement insertAdminStatement = connection
                    .prepareStatement("INSERT INTO user VALUES (null, ?, ?, null, null)", Statement.RETURN_GENERATED_KEYS);

            insertAdminStatement.setString(1, admin.getUsername());
            insertAdminStatement.setString(2, admin.getPassword());
            insertAdminStatement.executeUpdate();

            ResultSet rs = insertAdminStatement.getGeneratedKeys();

            if (rs.next()) {
                long userId = rs.getLong(1);
                admin.setId(userId);
                rightsRolesRepository.addRolesToUser(admin, admin.getRoles());
                System.out.println("Admin inserted successfully");

            } else {
                System.out.println("Failed to retrieve generated keys after user insertion.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during user insertion: " + e.getMessage());
        }
    }
}
