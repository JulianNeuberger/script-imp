package de.fsmpi.model.option;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;

@Entity
@DiscriminatorValue("boolean")
public class BooleanOption<V> extends Option<Boolean> {
    static {
        Option.addSubclass(BooleanOption.class);
    }

    @Override
    public boolean hasPossibleValues() {
        return true;
    }

    @Override
    public Iterable<String> getPossibleValues() {
        return Arrays.asList("TRUE", "FALSE");
    }

    @Override
    public Boolean getValueTypeSafe() {
        return Boolean.valueOf(this.getValue());
    }
}
