package com.gmail.dimabah;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Controller
public class MyController {
    private UserService userService;
    private PasswordEncoder encoder;

    public MyController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }
    @GetMapping("/")
    public String index (Model model){
        User user = getCurrentUser();

        String login = user.getUsername();
        CustomUser dbUser = userService.findByLogin(login);

        model.addAttribute("login",login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin", getRole(user));
        model.addAttribute("email",dbUser.getEmail());
        model.addAttribute("phone",dbUser.getPhone());
        model.addAttribute("address",dbUser.getAddress());
        model.addAttribute("avatar",dbUser.getAvatar());


        return "index";
    }
    @PostMapping(value = "/newuser")
    public String update(@RequestParam String login,
                         @RequestParam String pass,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                         Model model) {
        String passHash = encoder.encode(pass);

        if ( ! userService.addUser(login, passHash, UserRole.USER, email, phone, address)) {
            model.addAttribute("exists", true);
            model.addAttribute("login", login);
            return "register";
        }

        return "redirect:/";
    }
    @PostMapping(value = "/update")
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) MultipartFile file) {
        User user = getCurrentUser();
        String login = user.getUsername();
        String filePath = "D:/upload_dir/";
        String fileName = null;
        if (!"".equals(file.getOriginalFilename())) {
            try {
                File dir = new File("D:/upload_dir/");
                dir.mkdirs();
                fileName = login + file.getOriginalFilename();
                file.transferTo(new File(filePath+fileName));
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.updateUser(login, email, phone, address, fileName);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = getCurrentUser();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        User user = getCurrentUser();
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("admin", getRole(user));
        model.addAttribute("login", user.getUsername());
        return "admin";
    }
    @GetMapping(value = "/change")
    public String change(@RequestParam String login, Model model) {
        CustomUser user = userService.findByLogin(login);
        model.addAttribute("userToChange", user);
        return "change";
    }
    @PostMapping (value = "/change/user")
    public String changeUser(@RequestParam String login,
                             @RequestParam String role,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String address) {

        userService.updateUser(login, UserRole.valueOf(role), email, phone, address);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("admin", "ADMIN");
        return "admin";
    }


    private User getCurrentUser() {
       return (User) SecurityContextHolder
               .getContext()
               .getAuthentication()
               .getPrincipal();
    }
    private String getRole(User user){
        Collection<GrantedAuthority> roles = user.getAuthorities();
        for (var i:roles ) {
            if ("ADMIN".equals(i.getAuthority())){
                return "ADMIN";
            }
            if ("MODERATOR".equals(i.getAuthority())){
                return "MODERATOR";
            }

        }
        return null;
    }
}
