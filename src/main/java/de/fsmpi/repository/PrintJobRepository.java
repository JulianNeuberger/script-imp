package de.fsmpi.repository;

import de.fsmpi.model.print.PrintJob;
import de.fsmpi.model.print.PrintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Julian on 07.12.2016.
 */
@Repository
public interface PrintJobRepository extends PagingAndSortingRepository<PrintJob, Long> {

    Page<PrintJob> findAll(Pageable pageable);

    Page<PrintJob> findByStatus(Pageable pageable, PrintStatus status);

    Page<PrintJob> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<PrintJob> findByStatusOrderByCreatedDateDesc(Pageable pageable, PrintStatus printStatus);
}
