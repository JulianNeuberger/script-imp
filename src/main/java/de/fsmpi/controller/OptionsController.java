package de.fsmpi.controller;

import de.fsmpi.model.option.Option;
import de.fsmpi.repository.OptionRepository;
import de.fsmpi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/options")
public class OptionsController extends BaseController {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionsController(OptionRepository optionRepository,
                             NotificationService notificationService) {
        super(notificationService);
        this.optionRepository = optionRepository;
    }

    @RequestMapping("/show")
    public String show(Model model) {
        model.addAttribute("options", this.optionRepository.findAll());

        return "/pages/admin/edit-options";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@RequestParam("type") String classType,
                       @RequestParam("name") String name,
                       @RequestParam("value") String value) {
        Option option = null;
        try {
            option = (Option) Class.forName(classType).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
        option.setName(name);
        option.setValue(value);
        this.optionRepository.save(option);

        return "redirect:/options/show";
    }

    @RequestMapping("/edit")
    public String edit(Model model, @RequestParam String name) {
        model.addAttribute("option", this.optionRepository.findOne(name));

        return "/pages/admin/edit-single-option";
    }

    @RequestMapping("/add")
    public String add(Model model, @RequestParam String optionClassName) {
        try {
            model.addAttribute("option", Class.forName(optionClassName).getConstructor().newInstance());
        } catch (InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException
                | ClassNotFoundException e) {
            e.printStackTrace();
            // TODO: wtf happened here? maybe inform the user,
            // TODO: although every option should have an empty constructor, since it's an entity...
        }
        return "/pages/admin/edit-single-option";
    }

    @ModelAttribute("optionClasses")
    public void optionClasses(Model model) {
        model.addAttribute("optionClasses", Option.getSubClasses());
    }

    @ModelAttribute("defaultNames")
    public void defaultNames(Model model) {
        model.addAttribute("defaultNames", Option.getDefaultOptionNames());
    }
}
