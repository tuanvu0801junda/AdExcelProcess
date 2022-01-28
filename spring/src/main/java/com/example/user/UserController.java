package com.example.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // ************** ADD **************
    @PostMapping(path = "/add")
    public @ResponseBody User addNewUser(@RequestParam String name, @RequestParam String email, @RequestParam String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    // ************** SEARCH & GET ALL **************
    @GetMapping("/find/{id}")
    public User getOneUser (@PathVariable int id) {
        return userRepository.findById(id).orElse(null);
    }

    @GetMapping(path = "/find/all")
    public @ResponseBody Iterable<User> getAllUser(){
        //This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    // ************** UPDATE **************
    @PutMapping("/update/{id}")
    public User update(@PathVariable int id, @RequestBody User user) {
        User oldUser =  userRepository.findById(id).orElse(null);
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        return userRepository.save(oldUser);
    }

    // ************** DELETE **************
    @DeleteMapping("/delete/{id}")
    public void destroy(@PathVariable Integer id){
        userRepository.deleteById(id);
    }

}