package com.thinksmart.controller;

import com.thinksmart.database.User;
import com.thinksmart.database.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thinksmart.database.UserActivityRepository;
import java.time.LocalDateTime;
import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserActivityRepository activityRepository;

    @GetMapping("/home")
    public String homePage(Model model, jakarta.servlet.http.HttpSession session) {

        if(session.getAttribute("loggedInUser") == null){
            return "redirect:/login";
        }
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("username", user.getName());

        return "home";
    }


    @GetMapping("/math")
    public String mathPage(jakarta.servlet.http.HttpSession session) {

        if(session.getAttribute("loggedInUser") == null){
            return "redirect:/login";
        }

        return "math";
    }


    @GetMapping("/flashcards")
    public String flashcardsPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if(user == null){
            return "redirect:/login";
        }

        // ✅ Save activity
        UserActivity activity = new UserActivity();
        activity.setEmail(user.getEmail());
        activity.setTopic("FLASHCARD");
        activity.setProblem("Studied Flashcards");
        activity.setSolvedAt(LocalDateTime.now());
        activityRepository.save(activity);

        // ✅ FETCH ALL ACTIVITIES
        List<UserActivity> activities =
                activityRepository.findByEmail(user.getEmail());

        // ✅ CALCULATE STREAK (same logic as learning-progress)
        Set<LocalDate> activeDays = new HashSet<>();

        for(UserActivity a : activities){
            activeDays.add(a.getSolvedAt().toLocalDate());
        }

        int streak = 0;
        LocalDate today = LocalDate.now();

        while(activeDays.contains(today.minusDays(streak))){
            streak++;
        }

        // ✅ SEND TO FRONTEND
        model.addAttribute("streak", streak);

        return "flashcards";
    }

    @GetMapping("/practice-test")
    public String practiceTest(jakarta.servlet.http.HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if(user == null){
            return "redirect:/login";
        }

        UserActivity activity = new UserActivity();
        activity.setEmail(user.getEmail());
        activity.setTopic("PRACTICE_TEST");
        activity.setProblem("Attempted Practice Test");
        activity.setSolvedAt(LocalDateTime.now());

        activityRepository.save(activity);

        return "practice-test";
    }

    @GetMapping("/formulas")
    public String formulasPage(jakarta.servlet.http.HttpSession session) {

        if(session.getAttribute("loggedInUser") == null){
            return "redirect:/login";
        }
        return "formulas";
    }

    @GetMapping("/profile")
    public String profile(Model model, jakarta.servlet.http.HttpSession session){

        User user = (User) session.getAttribute("loggedInUser");

        if(user == null){
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        String email = user.getEmail();

        long mathSolved =
                activityRepository.countByEmailAndTopic(email, "LINEAR") +
                        activityRepository.countByEmailAndTopic(email, "QUADRATIC") +
                        activityRepository.countByEmailAndTopic(email, "CUBIC") +
                        activityRepository.countByEmailAndTopic(email, "SIMULTANEOUS") +
                        activityRepository.countByEmailAndTopic(email, "LIMIT") +
                        activityRepository.countByEmailAndTopic(email, "DIFFERENTIATION") +
                        activityRepository.countByEmailAndTopic(email, "INTEGRATION");

        long flashcardsStudied =
                activityRepository.countByEmailAndTopic(email, "FLASHCARD");

        long testsTaken =
                activityRepository.countByEmailAndTopic(email, "PRACTICE_TEST");

        model.addAttribute("mathSolved", mathSolved);
        model.addAttribute("flashcardsStudied", flashcardsStudied);
        model.addAttribute("testsTaken", testsTaken);

        return "profile";
    }

    @GetMapping("/learning-progress")
    public String learningProgress(Model model,
                                   jakarta.servlet.http.HttpSession session){

        User user = (User) session.getAttribute("loggedInUser");

        if(user == null){
            return "redirect:/login";
        }

        List<UserActivity> activities =
                activityRepository.findByEmail(user.getEmail());

        Set<LocalDate> activeDays = new HashSet<>();

        for(UserActivity a : activities){
            activeDays.add(a.getSolvedAt().toLocalDate());
        }

        int streak = 0;
        LocalDate today = LocalDate.now();

        while(activeDays.contains(today.minusDays(streak))){
            streak++;
        }

        Map<String,Integer> topicStats = new HashMap<>();

        for(UserActivity a : activities){

            String topic = a.getTopic();

            topicStats.put(topic, topicStats.getOrDefault(topic,0) + 1);

        }
        model.addAttribute("activities", activities);
        model.addAttribute("topicStats", topicStats);
        model.addAttribute("streak", streak);

        return "learning-progress";
    }
}
