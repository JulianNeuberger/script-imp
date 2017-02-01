package de.fsmpi.model.document;

/**
 * Created by Julian on 27.01.2017.
 */
public enum ExamForm {
    ORAL("mündlich"), WRITTEN("schriftlich");

    public final String name;

    private ExamForm(String name) {
        this.name = name;
    }
}
