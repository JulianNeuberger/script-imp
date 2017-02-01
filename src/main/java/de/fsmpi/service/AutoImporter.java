package de.fsmpi.service;

import de.fsmpi.model.document.*;
import de.fsmpi.repository.DocumentRepository;
import de.fsmpi.repository.ExamTypeRepository;
import de.fsmpi.repository.LectureRepository;
import de.fsmpi.repository.LecturerRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * This can be used to auto import existing documents into the system
 * FIXME: make this configurable on options screen
 */
@Service
public class AutoImporter {
    private static final Pattern SUMMER_TERM = Pattern.compile("(sommer(.semester)?|ss)(.)?(\\d{2,4})");
    private static final Pattern WINTER_TERM = Pattern.compile("(winter(.semester)?|ws)(.)?(\\d{2,4})");

    private final DocumentRepository documentRepository;
    private final LectureRepository lectureRepository;
    private final LecturerRepository lecturerRepository;
    private final ExamTypeRepository examTypeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoImporter.class);

    @Autowired
    public AutoImporter(DocumentRepository documentRepository,
                        LectureRepository lectureRepository,
                        LecturerRepository lecturerRepository,
                        ExamTypeRepository examTypeRepository) {
        this.documentRepository = documentRepository;
        this.lectureRepository = lectureRepository;
        this.lecturerRepository = lecturerRepository;
        this.examTypeRepository = examTypeRepository;
    }

    /**
     * Pass a java.io.File representing a root directory on which to do the import.
     * Expects a file structure, where there are folders with lecture names at top level,
     * in which there are folders representing lecturers and finally documents inside them
     * @param directory a directory in which to look for docs
     */
    public void importFromDirectory(File directory) {
        Long filesFound = 0L;

        File[] examForms = directory.listFiles();
        if(examForms == null) {
            // TODO: proper error handling
            return;
        }
        for (File examFormFile : examForms) {
            // ExamForm -> ORAL/WRITTEN
            ExamForm examForm = this.getExamForm(examFormFile.getName());

            File[] examTypes = examFormFile.listFiles();
            if(examTypes == null) {
                continue;
            }
            for (File examTypeFile : examTypes) {
                // the exam type denoted by folder name (eg. German Diplom, exam, questionnaire...)
                ExamType examType = this.getExamTypeOrCreate(examTypeFile.getName());

                File[] lectureFiles = examTypeFile.listFiles();
                if(lectureFiles == null) {
                    // no lectures for this exam type
                    continue;
                }
                for (File lectureFile : lectureFiles) {
                    Lecture lecture = this.getLectureOrCreate(lectureFile.getName());

                    File[] lecturerFiles = lectureFile.listFiles();
                    if(lecturerFiles == null) {
                        // no lecturers for this lecture
                        continue;
                    }
                    for (File lecturerFile : lecturerFiles) {
                        Lecturer lecturer = this.getLecturerOrCreate(lecturerFile.getName());

                        File[] documentFiles = lecturerFile.listFiles();
                        if(documentFiles == null) {
                            continue;
                        }
                        for (File documentFile : documentFiles) {
                            documentFile = this.skipSecondaryLecturers(documentFile);
                            if(documentFile == null) {
                                continue;
                            }

                            String docName = documentFile.getName();
                            Collection<Document> documents =
                                    this.documentRepository.findByNameAndLecturerAndLecture(docName, lecturer, lecture);
                            if(documents.size() != 0) {
                                // we already know this doc!
                                continue;
                            }
                            // we don't know it, create it
                            byte[] data;
                            try {
                                FileInputStream inputStream = new FileInputStream(documentFile);
                                data = IOUtils.toByteArray(inputStream);
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                                // FIXME: Retry
                                continue;
                            }
                            LOGGER.info(MessageFormat.format("Unknown document named {0} held by {1} in {2}, importing.", docName, lecturer.getName(), lecture.getName()));
                            Document document = new Document();
                            document.setName(docName);
                            document.setLecturer(lecturer);
                            document.setLecture(lecture);
                            document.setExamForm(examForm);
                            document.setExamType(examType);
                            document.setFilePath(copyMoveFile(documentFile, document));
                            this.documentRepository.save(document);

                            ++filesFound;
                        }
                    }
                }
            }
        }

        LOGGER.info(MessageFormat.format("Found {0} new documents", filesFound));
    }

    private ExamForm getExamForm(String examFormName) {
        ExamForm examForm = ExamForm.ORAL;
        if(examFormName.equals("schriftlich")) {
            examForm = ExamForm.WRITTEN;
        }
        return examForm;
    }

    private ExamType getExamTypeOrCreate(String examTypeName) {
        ExamType examType = this.examTypeRepository.findOne(examTypeName);
        if(examType == null) {
            examType = new ExamType();
            examType.setName(examTypeName);
            examType = this.examTypeRepository.save(examType);
        }
        return examType;
    }

    private Lecture getLectureOrCreate(String lectureName) {
        Collection<Lecture> lectures = this.lectureRepository.findByName(lectureName);
        Lecture lecture;
        if(lectures.size() == 0) {
            lecture = new Lecture();
            lecture.setName(lectureName);
            return this.lectureRepository.save(lecture);
        } else {
            // maybe picking the first lecture we find isn't the best idea,
            // but it's the only one I have right now
            return lectures.stream().findFirst().get();
        }
    }

    private Lecturer getLecturerOrCreate(String lecturerName) {
        Collection<Lecturer> lecturers = this.lecturerRepository.findByName(lecturerName);
        Lecturer lecturer;
        if(lecturers.size() == 0) {
            lecturer = new Lecturer();
            lecturer.setName(lecturerName);
            return this.lecturerRepository.save(lecturer);
        } else {
            // still: may not be a smart idea
            return lecturers.stream().findFirst().get();
        }
    }

    private File skipSecondaryLecturers(File curFile) {
        boolean foundFile;
        while(!curFile.isFile()) {
            // this is ugly business logic
            File[] possibleFiles = curFile.listFiles();
            if(possibleFiles != null) {
                if(possibleFiles.length > 0) {
                    curFile = possibleFiles[0];
                    foundFile = curFile.isFile();
                    if(foundFile) {
                        return curFile;
                    }
                } else {
                    // that's it, no more file --> empty folder, continue
                    return null;
                }
            }
        }

        return curFile;
    }

    private String copyMoveFile(File file, Document document) {
        //noinspection StringBufferReplaceableByString
        StringBuilder newFilePath = new StringBuilder("pdf")
                .append('/')
                .append(document.getExamForm().name)
                .append('/')
                .append(document.getLecture().getName())
                .append('/')
                .append(document.getExamType().getName())
                .append('/')
                .append(document.getLecturer().getName())
                .append('/')
                .append(document.getName());
        File newFile = new File(newFilePath.toString());
        if(!newFile.getParentFile().exists()) {
            if(!newFile.getParentFile().mkdirs()) {
                throw new AssertionError("Could not create directories: " + newFilePath);
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)){
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                while(fileInputStream.available() > 0) {
                    fileOutputStream.write(fileInputStream.read());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFilePath.toString();
    }

    private String getSemesterFromName(String name) {
        return null;
    }
}
