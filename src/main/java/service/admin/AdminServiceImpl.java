package service.admin;

import model.User;
import model.validator.Notification;
import repository.admin.AdminRepository;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public Notification<Boolean> saveEmployee(User employee)
    {
        return adminRepository.saveEmployee(employee);
    }

    @Override
    public List<User> findAllEmployees() {
        return adminRepository.findAllEmployees();
    }
    @Override
    public boolean updateEmployee(int id, String newUsername) {
        return adminRepository.updateEmployee(id, newUsername);
    }
    @Override
    public boolean deleteEmployeeByUsername(String username)
    {
        return adminRepository.deleteEmployeeByUsername(username);
    }
    @Override
    public Map<User,String> findEmployeeActivity()
    {
        return adminRepository.findEmployeeActivity();
    }
}
