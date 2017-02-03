package de.fsmpi.model.option;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Julian on 26.01.2017.
 */
@Entity
@DiscriminatorValue("integer")
public class IntegerOption<V> extends Option<Integer> {
	static {
		Option.addSubclass(IntegerOption.class);
	}

	@Override
	public Integer getValueTypeSafe() {
		return Integer.valueOf(this.value);
	}
}
