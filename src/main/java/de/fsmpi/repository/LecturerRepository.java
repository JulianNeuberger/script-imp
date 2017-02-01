package de.fsmpi.repository;

import de.fsmpi.model.document.Lecturer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by Julian on 07.12.2016.
 */
@Repository
public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, Long> {

    public Collection<Lecturer> findByName(String name);
}
