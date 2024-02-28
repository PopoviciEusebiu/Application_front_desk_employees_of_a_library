package repository.superAdmin;

import model.User;

import java.sql.SQLException;

public interface SuperAdminRepository {

    void saveAdmin(User admin);
}
