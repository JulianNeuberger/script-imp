package de.fsmpi.model.document;

/**
 * Created by Julian on 09.12.2016.
 */
public enum Field {

	MATH("Mathe"), PHYSICS("Physik"), COMPUTER_SCIENCE("Info");

	protected final String name;

	private Field(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
