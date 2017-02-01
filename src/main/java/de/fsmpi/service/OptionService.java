package de.fsmpi.service;

import de.fsmpi.model.option.Option;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public interface OptionService {
    Option getOptionByName(String name);

    void setOptionByName(String name, Serializable value);

    void setupDefaultOptions();
}
