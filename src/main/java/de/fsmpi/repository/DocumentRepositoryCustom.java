package de.fsmpi.repository;

import de.fsmpi.model.document.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Created by Julian on 27.01.2017.
 */
public interface DocumentRepositoryCustom {
    public Collection<Document> search(String partialName,
                                       Lecturer lecturer,
                                       Lecture lecture,
                                       ExamForm examForm,
                                       ExamType examType);

    public Page<Document> search(String partialName,
                                 Lecturer lecturer,
                                 Lecture lecture,
                                 ExamForm examForm,
                                 ExamType examType,
                                 Pageable pageable);
}
