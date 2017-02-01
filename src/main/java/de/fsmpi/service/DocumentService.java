package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Julian on 25.01.2017.
 */
@Service
public interface DocumentService {

    void saveDocumentWithFile(Document document, MultipartFile file);
}
