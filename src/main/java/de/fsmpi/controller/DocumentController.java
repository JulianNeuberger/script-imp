package de.fsmpi.controller;

import de.fsmpi.model.document.*;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.repository.ExamTypeRepository;
import de.fsmpi.repository.LectureRepository;
import de.fsmpi.repository.LecturerRepository;
import de.fsmpi.service.AutoImporter;
import de.fsmpi.service.CartService;
import de.fsmpi.service.DocumentService;
import de.fsmpi.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.MessageFormat;

@Controller
@RequestMapping("/documents")
public class DocumentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentRepository documentRepository;
    private final LecturerRepository lecturerRepository;
    private final ExamTypeRepository examTypeRepository;
    private final LectureRepository lectureRepository;

    private final AutoImporter autoImporter;
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentRepository documentRepository,
                              LecturerRepository lecturerRepository,
                              LectureRepository lectureRepository,
                              ExamTypeRepository examTypeRepository,
                              NotificationService notificationService,
                              CartService cartService,
                              DocumentService documentService,
                              AutoImporter autoImporter) {
        super(notificationService, cartService);
        this.documentRepository = documentRepository;
        this.lecturerRepository = lecturerRepository;
        this.lectureRepository = lectureRepository;
        this.examTypeRepository = examTypeRepository;
        this.documentService = documentService;
        this.autoImporter = autoImporter;
    }

    @RequestMapping(path = "/show", params = {"id"})
    public String showSingleDocument(Model model,
                                     @RequestParam(name = "id") Long documentId,
                                     @RequestParam(name = "backLink") String backLink) {
        Document document = this.documentRepository.findOne(documentId);

        model.addAttribute("document", document);
        model.addAttribute("backLink", backLink);

        return "/pages/user/show-single-document";
    }

    @ResponseBody
    @RequestMapping(path = "/data", params = {"id"}, method = RequestMethod.GET, produces = "application/pdf")
    public FileSystemResource getDocumentData(@RequestParam(name = "id") Long documentId) {
        return new FileSystemResource(documentRepository.findOne(documentId).getFilePath());
    }

    @RequestMapping(path = "/show")
    public String showDocuments(Model model,
                                HttpServletRequest request,
                                @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
                                @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                @RequestParam(name = "examForm", required = false) ExamForm examForm,
                                @RequestParam(name = "examTypeId", required = false) String examTypeId,
                                @RequestParam(name = "lecturerId", required = false) Long lecturerId,
                                @RequestParam(name = "lectureId", required = false) Long lectureId,
                                @RequestParam(name = "keyword", required = false) String keyword) {
        long start = System.currentTimeMillis();
        Lecture lecture = null;
        Lecturer lecturer = null;
        ExamType examType = null;
        if(examTypeId != null) {
            examType = this.examTypeRepository.findOne(examTypeId);
        }
        if(lectureId != null) {
            lecture = this.lectureRepository.findOne(lectureId);
        }
        if(lecturerId != null) {
            lecturer = this.lecturerRepository.findOne(lecturerId);
        }

        long tmp = System.currentTimeMillis();
        Page<Document> documents
                = documentRepository.search(keyword, lecturer, lecture, examForm, examType, new PageRequest(page, size));
        LOGGER.info(
            MessageFormat.format(
                "It took {0}ms to retrieve {1} documents.",
                (System.currentTimeMillis() - tmp),
                documents.getSize()
            )
        );

        model.addAttribute("documents", documents);
        model.addAttribute("size", documents.getSize());
        model.addAttribute("page", documents.getNumber());
        model.addAttribute("totalPages", documents.getTotalPages());
        model.addAttribute("examForm", examForm);
        model.addAttribute("examTypeId", examTypeId);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("lectureId", lectureId);
        model.addAttribute("name", keyword);

        // full link to current view
        model.addAttribute("pageLink", request.getRequestURL().toString() + "?" + request.getQueryString());

        LOGGER.info(MessageFormat.format("Total exec time for showDocuments is {0}ms", System.currentTimeMillis() - start));
        return "/pages/user/show-documents";
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addDocument(Model model) {
        model.addAttribute("document", new Document());
        return "/pages/admin/edit-document";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String editDocument(Model model, @RequestParam Long id) {
        model.addAttribute("document", this.documentRepository.findOne(id));
        return "/pages/admin/edit-document";
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String saveDocument( @ModelAttribute Document document,
                                @RequestParam("file")MultipartFile file) {
        this.documentService.saveDocumentWithFile(document, file);
        return "redirect:/documents/edit?id=" + document.getId();
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST, params = {"next"})
    public String saveDocumentAndNext(  @ModelAttribute Document document,
                                        @RequestParam("file")MultipartFile file) {
        this.documentService.saveDocumentWithFile(document, file);
        return "redirect:/documents/add";
    }

    @RequestMapping(path = "/import")
    public String autoImport(@RequestParam("dir") String baseDir) {
        this.autoImporter.importFromDirectory(new File(baseDir));
        return "redirect:/documents/show";
    }

    // #####################################
    // MODEL ATTRIBUTES
    // #####################################
    @ModelAttribute("examForms")
    public void examForms(Model model) {
        model.addAttribute("examForms", ExamForm.values());
    }

    @ModelAttribute("examTypes")
    public void examTypes(Model model) {
        model.addAttribute("examTypes", this.examTypeRepository.findAll());
    }

    @ModelAttribute("lecturers")
    public void lecturers(Model model) {
        model.addAttribute("lecturers", this.lecturerRepository.findAll());
    }

    @ModelAttribute("lectures")
    public void lectures(Model model) {
        model.addAttribute("lectures", this.lectureRepository.findAll());
    }
}