package service.admin;

import model.User;
import model.validator.Notification;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Notification<Boolean> saveEmployee(User employee);
    List<User> findAllEmployees();
    boolean updateEmployee(int id, String newUsername);
    boolean deleteEmployeeByUsername(String username);
    Map<User,String> findEmployeeActivity();


}
