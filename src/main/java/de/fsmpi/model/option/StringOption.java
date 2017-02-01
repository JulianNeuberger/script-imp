package de.fsmpi.model.option;

import de.fsmpi.model.option.Option;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Julian on 26.01.2017.
 */
@Entity
@DiscriminatorValue("string")
public class StringOption<V> extends Option<String> {
    @Override
    public String getValueTypeSafe() {
        return this.value;
    }
}
