package de.fsmpi.service;

import de.fsmpi.model.document.Lecture;
import de.fsmpi.model.document.Lecturer;
import de.fsmpi.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Julian on 25.01.2017.
 */
@Service
public class LectureServiceImpl implements LectureService {

    private final LecturerRepository lecturerRepository;

    @Autowired
    public LectureServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public void updateLecturers(Lecture lecture) {
        Set<Lecturer> lecturers = lecture.getLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturer.getLectures().add(lecture);
        }
        this.lecturerRepository.save(lecturers);
    }
}
