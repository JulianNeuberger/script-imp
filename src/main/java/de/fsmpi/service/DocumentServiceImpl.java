package de.fsmpi.service;

import de.fsmpi.model.document.Document;
import de.fsmpi.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Julian on 26.01.2017.
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void saveDocumentWithFile(Document document, MultipartFile file) {
        boolean uploadSuccess = false;
        if(file.isEmpty()) {
            uploadSuccess = true;
        } else {
            //            if(file.getContentType())
            uploadSuccess = true;
        }
        documentRepository.save(document);
    }
}
