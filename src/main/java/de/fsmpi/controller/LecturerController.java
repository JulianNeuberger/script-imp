package de.fsmpi.controller;

import de.fsmpi.model.document.Lecturer;
import de.fsmpi.repository.LectureRepository;
import de.fsmpi.repository.LecturerRepository;
import de.fsmpi.service.CartService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lecturers")
public class LecturerController extends BaseController {

    private final LecturerRepository lecturerRepository;
    private final LectureRepository lectureRepository;

    @Autowired
    public LecturerController(LecturerRepository lecturerRepository,
                              LectureRepository lectureRepository,
                              NotificationService notificationService,
                              CartService cartService) {
        super(notificationService, cartService);
        this.lecturerRepository = lecturerRepository;
        this.lectureRepository = lectureRepository;
    }

    @RequestMapping(path = "/show")
    public String show(Model model) {
        model.addAttribute("lecturers", lecturerRepository.findAll());
        return "/pages/user/show-lecturers";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") Long id) {
        Lecturer lecturer = this.lecturerRepository.findOne(id);
        model.addAttribute(lecturer);
        return "/pages/admin/edit-lecturer";
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        Lecturer lecturer = new Lecturer();
        model.addAttribute("lecturer", lecturer);

        return "/pages/admin/edit-lecturer";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String add(@ModelAttribute Lecturer lecturer) {
        lecturerRepository.save(lecturer);

        return "redirect:/lecturers/edit?" + lecturer.getId();
    }

    @ModelAttribute("lectures")
    public void lectures(Model model) {
        model.addAttribute("lectures", this.lectureRepository.findAll());
    }
}
