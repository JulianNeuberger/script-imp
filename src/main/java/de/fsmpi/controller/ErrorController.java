package de.fsmpi.controller;

import de.fsmpi.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**
 * Created by Julian on 25.01.2017.
 */
//@Controller
public class ErrorController extends BaseController {

    public ErrorController(NotificationService notificationService) {
        super(notificationService);
    }

//    @ExceptionHandler(Exception.class)
    public String error(Map<String, Object> model, Exception e) {

        model.put("exceptionMessage", e);
        return "/pages/user/error.ftl";
    }
}
