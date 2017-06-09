package de.fsmpi.controller;

import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;

import java.util.Map;

//@Controller
public class ErrorController extends BaseController {

    public ErrorController(NotificationService notificationService,
                           CartService cartService) {
        super(notificationService, cartService);
    }

//    @ExceptionHandler(Exception.class)
    public String error(Map<String, Object> model, Exception e) {
        model.put("exceptionMessage", e);
        return "pages/user/error.ftl";
    }
}
