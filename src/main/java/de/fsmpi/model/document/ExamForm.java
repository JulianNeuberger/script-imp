package de.fsmpi.model.document;

public enum ExamForm {
	ORAL("document.form.oral"), WRITTEN("document.form.written");

	public final String key;

	ExamForm(String key) {
		this.key = key;
	}
}
