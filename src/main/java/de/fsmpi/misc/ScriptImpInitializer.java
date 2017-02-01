package de.fsmpi.misc;

import de.fsmpi.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ScriptImpInitializer {

    private final OptionService optionService;

    @Autowired
    public ScriptImpInitializer(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostConstruct
    public void initScriptImp() {
        optionService.setupDefaultOptions();
    }
}
