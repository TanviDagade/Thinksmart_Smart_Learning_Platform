package com.thinksmart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import com.thinksmart.service.HybridAIDoubtService;

@Controller
@RequestMapping("/ai-doubt")
public class AIDoubtController {

    private final HybridAIDoubtService aiService;

    public AIDoubtController(HybridAIDoubtService aiService) {
        this.aiService = aiService;
    }

    @GetMapping
    public String showPage(HttpSession session, Model model) {

        List<String> history = (List<String>) session.getAttribute("chatHistory");

        if (history == null) {
            history = new ArrayList<>();
            history.add("AI: Hello! I'm ThinkSmart AI. Ask me any math or concept question.");
            session.setAttribute("chatHistory", history);
        }

        model.addAttribute("chatHistory", history);
        return "ai-doubt";
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam("question") String question,
                              HttpSession session,
                              Model model) {

        List<String> history = (List<String>) session.getAttribute("chatHistory");

        if (history == null) {
            history = new ArrayList<>();
        }

        String response;

        try {
            // ✅ Direct call (NO finalPrompt, NO context builder)
            response = aiService.processQuestion(question, session);

        } catch (Exception e) {
            response = "Sorry, I encountered an issue. Please try again.";
        }

        // ✅ Save to history
        history.add("USER: " + question);
        history.add("AI: " + response);

        // ✅ Trim history safely (FIXED — prevents subList corruption)
        if (history.size() > 20) {
            history = new ArrayList<>(history.subList(history.size() - 20, history.size()));
        }

        session.setAttribute("chatHistory", history);
        model.addAttribute("chatHistory", history);

        return "ai-doubt";
    }

    @PostMapping("/clear")
    public String clearChat(HttpSession session) {

        List<String> history = new ArrayList<>();
        history.add("AI: Chat cleared. Ask me something new!");

        session.setAttribute("chatHistory", history);

        return "redirect:/ai-doubt";
    }

    @PostMapping("/new")
    public String newChat(HttpSession session) {
        session.invalidate(); // Completely reset session
        return "redirect:/ai-doubt";
    }

    // ✅ Simple math detection (optional use in future)
    private boolean isMathQuestion(String input) {
        return input != null && input.matches(".*[0-9xX+\\-*/^=()].*");
    }

    // ✅ Optional topic detection (unused but safe)
    private String detectTopic(String question) {
        question = question.toLowerCase();
        if (question.matches(".*[0-9xX+\\-*/^=()].*")) return "Math";
        if (question.contains("java") || question.contains("python") || question.contains("code")) return "Coding";
        return "Theory";
    }

    // ✅ Optional follow-ups (unused but safe)
    private List<String> generateFollowUps(String topic, String question) {
        List<String> suggestions = new ArrayList<>();

        switch (topic) {
            case "Math":
                suggestions.add("Try solving a similar problem with different numbers.");
                suggestions.add("Can you solve it using another method?");
                break;

            case "Coding":
                suggestions.add("Try implementing this in another language.");
                suggestions.add("Can you optimize this code?");
                break;

            case "Theory":
                suggestions.add("Can you explain it with an example?");
                suggestions.add("How does it apply in real life?");
                break;
        }

        return suggestions;
    }

    // ✅ Clarification detection (NOT auto-triggered anymore)
    private boolean needsClarification(String question) {
        String[] vagueWords = {"explain", "tell", "something", "help"};
        for (String word : vagueWords) {
            if (question.toLowerCase().contains(word)) return true;
        }
        return false;
    }
}