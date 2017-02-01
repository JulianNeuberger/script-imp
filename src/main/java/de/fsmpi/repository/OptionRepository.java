package de.fsmpi.repository;

import de.fsmpi.model.option.Option;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends CrudRepository<Option, String> {
}
