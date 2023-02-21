package com.gmail.dimabah;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public List<CustomUser> findAllUsers(){
        return userRepository.findAll();
    }
    @Transactional (readOnly = true)
    public CustomUser findByLogin (String login){
       return userRepository.findByLogin(login);
    }
    @Transactional
    public void deleteUsers(List<Long> listOfId){
        listOfId.forEach(id->{
            Optional<CustomUser>user =userRepository.findById(id);
            user.ifPresent(u-> {
                if (!u.getLogin().equals(AppConfig.ADMIN)){
                    userRepository.delete(u);
                }
            });
        });
    }
    @Transactional
    public boolean addUser(String login, String pass, UserRole role,
                           String email, String phone, String address){
        if (userRepository.existsByLogin(login)){
            return false;
        }

        CustomUser user = new CustomUser(login,pass,role,email,phone,address);
        userRepository.save(user);

        return true;
    }
    @Transactional
    public void updateUser(String login, String email, String phone, String address, String filePath){
        CustomUser user = findByLogin(login);
        if (user==null){
            return;
        }

        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        if (filePath!=null) {
            user.setAvatar(filePath);
        }
        userRepository.save(user);
    }
    @Transactional
    public void updateUser(String login, UserRole role, String email, String phone, String address){
        CustomUser user = findByLogin(login);
        if (user==null){
            return;
        }
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);
    }

}
