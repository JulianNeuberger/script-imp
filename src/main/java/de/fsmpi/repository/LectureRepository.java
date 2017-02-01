package de.fsmpi.repository;

import de.fsmpi.model.document.Lecture;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by Julian on 07.12.2016.
 */
@Repository
public interface LectureRepository extends PagingAndSortingRepository<Lecture, Long> {
    public Collection<Lecture> findByName(String name);
}