package de.fsmpi.service;

import de.fsmpi.model.document.Lecture;
import org.springframework.stereotype.Service;

/**
 * Created by Julian on 25.01.2017.
 */
@Service
public interface LectureService {

    public void updateLecturers(Lecture lecture);
}
