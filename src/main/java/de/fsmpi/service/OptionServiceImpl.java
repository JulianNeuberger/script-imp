package de.fsmpi.service;

import de.fsmpi.model.option.Option;
import de.fsmpi.repository.OptionRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option getOptionByName(String name) {
        return this.optionRepository.findOne(name);
    }

    @Override
    public void setOptionByName(String name, Serializable value) {
        Option option = this.getOptionByName(name);
        option.setValue(value.toString());
    }

    @Override
    public void setupDefaultOptions() {
        for (Pair<String, Class<? extends Option>> optionTemplate : Option.DEFAULT_OPTIONS) {
            String optionName = optionTemplate.getKey();
            boolean exists = optionRepository.exists(optionName);
            if(!exists) {
                Option option;
                try {
                    option = optionTemplate.getValue().getConstructor().newInstance();
                } catch (InstantiationException e) {
                    throw new AssertionError(
                            "Exception in constructor of class " +
                                optionTemplate.getValue(),
                            e);
                } catch (InvocationTargetException |
                        NoSuchMethodException |
                        IllegalAccessException e) {
                    throw new AssertionError("There is no public default constructor for class " +
                            optionTemplate.getValue() +
                            ". We need one though, please implement one!");
                }
                option.setName(optionName);
                this.optionRepository.save(option);
            }
        }
    }
}
