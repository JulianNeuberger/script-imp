package de.fsmpi.repository;

import de.fsmpi.model.document.Document;
import de.fsmpi.model.document.Lecture;
import de.fsmpi.model.document.Lecturer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by Julian on 07.12.2016.
 */
@Repository
public interface DocumentRepository extends CrudRepository<Document, Long>, DocumentRepositoryCustom {
    public Collection<Document> findByNameAndLecturerAndLecture(String name, Lecturer lecturer, Lecture lecture);
}
