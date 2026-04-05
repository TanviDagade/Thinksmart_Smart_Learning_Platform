package com.thinksmart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.thinksmart.database.User;
import com.thinksmart.database.UserRepository;
import java.time.LocalDateTime;
import com.thinksmart.security.OtpService;
import com.thinksmart.security.EmailService;

@Controller
public class AuthController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @PostMapping("/registerUser")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            jakarta.servlet.http.HttpSession session) {

        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
            model.addAttribute("error","Invalid email format");
            return "register";
        }

        // Check if email already exists
        if(userRepository.findByEmail(email) != null){
            model.addAttribute("error", "Email already registered. Please login.");
            return "register";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        String otp = OtpService.generateOtp();

        session.setAttribute("otp", otp);
        session.setAttribute("otpTime", LocalDateTime.now());
        session.setAttribute("name", name);
        session.setAttribute("email", email);
        session.setAttribute("password", password);

        emailService.sendOtp(email, otp);

        model.addAttribute("action","/verifyOtp");
        return "verify-otp";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            jakarta.servlet.http.HttpSession session) {

        User user = userRepository.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){

            session.setAttribute("loggedInUser", user);

            return "redirect:/home";
        }

        model.addAttribute("error","Invalid email or password");
        return "login";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(
            @RequestParam String otp,
            jakarta.servlet.http.HttpSession session,
            Model model){

        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpTime = (LocalDateTime) session.getAttribute("otpTime");

        if(otpTime == null || LocalDateTime.now().isAfter(otpTime.plusMinutes(2))){
            model.addAttribute("error","OTP expired. Please request a new OTP.");
            return "verify-otp";
        }

        if(!otp.equals(sessionOtp)){

            model.addAttribute("error","Invalid OTP");

            return "verify-otp";
        }

        String name = (String) session.getAttribute("name");
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        session.removeAttribute("otp");

        return "redirect:/login?registered=true";
    }

    @PostMapping("/resendOtp")
    public String resendOtp(jakarta.servlet.http.HttpSession session){

        String email = (String) session.getAttribute("email");

        String newOtp = OtpService.generateOtp();

        session.setAttribute("otp", newOtp);
        session.setAttribute("otpTime", LocalDateTime.now());

        emailService.sendOtp(email, newOtp);

        return "verify-otp";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String registered, Model model) {

        if(registered != null){
            model.addAttribute("success", "Account created successfully. Please login.");
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session){

        session.invalidate();

        return "redirect:/login";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam String newPassword,
            jakarta.servlet.http.HttpSession session){

        User user = (User) session.getAttribute("loggedInUser");

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return "redirect:/profile";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email,
                                       jakarta.servlet.http.HttpSession session,
                                       Model model) {

        User user = userRepository.findByEmail(email);

        if(user == null){
            model.addAttribute("error","Email not found!");
            return "forgot-password";
        }

        // generate OTP
        String otp = OtpService.generateOtp();

        // save in session
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        session.setAttribute("otpTime", LocalDateTime.now());

        // send email
        emailService.sendPasswordResetOtp(email, otp);

        model.addAttribute("email", email);
        model.addAttribute("action","/verify-forgot-otp");
        return "verify-otp";
    }

    @PostMapping("/verify-forgot-otp")
    public String verifyForgotOtp(@RequestParam String otp,
                                  @RequestParam String email,
                                  jakarta.servlet.http.HttpSession session,
                                  Model model){

        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpTime = (LocalDateTime) session.getAttribute("otpTime");

        if(sessionOtp == null || !sessionOtp.equals(otp)){
            model.addAttribute("error","Invalid OTP");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        // check expiry (2 minutes)
        if(LocalDateTime.now().isAfter(otpTime.plusMinutes(2))){
            model.addAttribute("error","OTP expired. Please request a new OTP.");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        // check OTP
        if(!sessionOtp.equals(otp)){
            model.addAttribute("error","Invalid OTP");
            model.addAttribute("email", email);
            return "verify-otp";
        }

        model.addAttribute("email", email);

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            Model model) {

        User user = userRepository.findByEmail(email);

        if(user == null){
            model.addAttribute("error","User not found!");
            return "forgot-password";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // redirect user to login page after reset
        model.addAttribute("message","Password updated successfully. Please login.");

        return "login";
    }

}

