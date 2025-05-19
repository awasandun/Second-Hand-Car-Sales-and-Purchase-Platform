package com.se1020.secondhandcarplatform.controller;

import com.se1020.secondhandcarplatform.model.Bid;
import com.se1020.secondhandcarplatform.model.Car;
import com.se1020.secondhandcarplatform.model.User;
import com.se1020.secondhandcarplatform.service.BidService;
import com.se1020.secondhandcarplatform.service.CarService;
import com.se1020.secondhandcarplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private BidService bidService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // Check if username already exists
        Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return "redirect:/users/register?error=username";
        }

        // Set role to USER by default
        user.setRole("USER");
        userService.saveUser(user);
        return "redirect:/users/login?registered";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> optionalUser = userService.authenticate(username, password);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            return "redirect:/";
        }
        return "redirect:/users/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);

            // Get user's cars
            List<Car> userCars = carService.getCarsBySellerId(userId);
            model.addAttribute("cars", userCars);

            // Get user's bids
            List<Bid> userBids = bidService.getBidsByBidderId(userId);
            model.addAttribute("bids", userBids);

            return "users/profile";
        }
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String showEditProfileForm(Model model, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "users/edit";
        }
        return "redirect:/";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute User user, HttpSession session) {
        // Check if user is logged in
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login";
        }

        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            // Update user information
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setPassword(user.getPassword());

            // Preserve username and role
            existingUser.setUsername(existingUser.getUsername());
            existingUser.setRole(existingUser.getRole());

            userService.saveUser(existingUser);
        }
        return "redirect:/users/profile";
    }
}
