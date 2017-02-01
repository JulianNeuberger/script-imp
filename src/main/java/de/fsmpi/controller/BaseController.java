package de.fsmpi.controller;

import de.fsmpi.model.user.User;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    protected final NotificationService notificationService;

    @Autowired
    public BaseController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute
    public void user(Model model) {
        model.addAttribute("user", getCurrentUserOrNull());
    }

    @ModelAttribute
    public void notifications(Model model) {
        User user = getCurrentUserOrNull();
        if(user != null) {
            model.addAttribute("hasNotifications", notificationService.hasNewNotificationsForUser(user));
            model.addAttribute("notifications", notificationService.getNewNotificationsForUser(user));
        } else {
            model.addAttribute("hasNotifications", false);
        }
    }

    protected User getCurrentUserOrNull() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User) {
            return (User) principal;
        } else {
            return null;
        }
    }
}
