package de.fsmpi.repository;

import de.fsmpi.model.print.PrintJobDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintJobDocumentRepository extends CrudRepository<PrintJobDocument, Long> {
}
