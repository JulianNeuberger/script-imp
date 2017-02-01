package de.fsmpi.controller;

import de.fsmpi.model.document.Field;
import de.fsmpi.model.document.Lecture;
import de.fsmpi.repository.LectureRepository;
import de.fsmpi.repository.LecturerRepository;
import de.fsmpi.service.LectureService;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lectures")
public class LectureController extends BaseController {

    private final LectureRepository lectureRepository;
    private final LecturerRepository lecturerRepository;
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureRepository lectureRepository,
                             LecturerRepository lecturerRepository,
                             NotificationService notificationService,
                             LectureService lectureService) {
        super(notificationService);
        this.lectureRepository = lectureRepository;
        this.lecturerRepository = lecturerRepository;
        this.lectureService = lectureService;
    }

    @RequestMapping(path = "/show")
    public String show(Model model) {
        model.addAttribute("lectures", lectureRepository.findAll());
        return "/pages/user/show-lectures";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") Long id) {
        Lecture lecture = this.lectureRepository.findOne(id);
        model.addAttribute(lecture);
        return "/pages/admin/edit-lecture";
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        Lecture lecture = new Lecture();
        model.addAttribute("lecture", lecture);
        return "/pages/admin/edit-lecture";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String add(@ModelAttribute Lecture lecture) {
        this.lectureRepository.save(lecture);
        this.lectureService.updateLecturers(lecture);

        return "redirect:/lectures/edit?id=" + lecture.getId();
    }

    @ModelAttribute("fields")
    public void fields(Model model) {
        model.addAttribute("fields", Field.values());
    }

    @ModelAttribute("lecturers")
    public void lecturers(Model model) {
        model.addAttribute("lecturers", this.lecturerRepository.findAll());
    }
}
