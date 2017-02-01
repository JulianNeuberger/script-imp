package de.fsmpi.repository;

import de.fsmpi.model.document.ExamType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Julian on 27.01.2017.
 */
@Repository
public interface ExamTypeRepository extends CrudRepository<ExamType, String> {
}
