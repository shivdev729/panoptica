package com.artifactexplorer.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/api/user")
@RestController
public class UserController{
    private final UserService userService;
    public UserController(UserService us){
        userService = us;
    }
    @GetMapping("/view")
    public String getUserByID(@RequestParam("id") String param) {
        // exception handling
        Long id = Long.decode(param);
        User user = this.userService.findUserByID(id);
        
        return user.getName();
    }
    
}