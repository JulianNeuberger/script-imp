package de.fsmpi.controller;

import de.fsmpi.model.user.Notification;
import de.fsmpi.repository.NotificationRepository;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * Created by Julian on 01.02.2017.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController extends BaseController {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationController(NotificationService notificationService,
                                  NotificationRepository notificationRepository,
                                  CartService cartService) {
        super(notificationService, cartService);
        this.notificationRepository = notificationRepository;
    }

    @RequestMapping(path = "/show")
    public String show(Model model) {
		Set<Notification> notifications = this.notificationService.getOldNotificationsForUser(getCurrentUserOrNull());
		model.addAttribute("oldNotifications", notifications);
		model.addAttribute("hasOldNotifications", notifications.size() > 0);
        return "pages/user/show-notifications";
    }

    @RequestMapping(path = "/read")
    public String read(Model model,
                       @RequestParam("id") Long notificationId) {
        Notification notification = this.notificationRepository.findOne(notificationId);
        this.notificationService.mark(notification, true);
        return "redirect:" + notification.getTarget();
    }

    @RequestMapping(path = "/toggle")
    public String toggleRead(Model model,
                       @RequestParam("id") Long notificationId) {
        Notification notification = this.notificationRepository.findOne(notificationId);
        this.notificationService.mark(notification, !notification.getRead());
        return "redirect:/notifications/show";
    }

    @RequestMapping(path = "/readall")
    public String readAll(Model model) {
        this.notificationService.markAll(getCurrentUserOrNull(), true);
        return "redirect:/notifications/show";
    }
}
