package de.fsmpi.model.document;

import org.hibernate.annotations.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;

/**
 * Created by Julian on 07.12.2016.
 */
@Entity
@Table
public class Document {

    public Document() {}

    @Id
    @Column
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Lecture lecture;

    @ManyToOne
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Lecturer lecturer;

    @ManyToOne
    @JoinColumn
    protected ExamType examType;

    @Column
    @Enumerated(EnumType.STRING)
    protected ExamForm examForm;

    @Column
    protected String semester;

    @Column
    protected String name;

    @Column
    protected String filePath;

    @Column
    protected byte[] thumbnail;

    @Column
    @ColumnDefault(value = "0")
    protected Integer pages;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime uploadDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public DateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(DateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public ExamForm getExamForm() {
        return examForm;
    }

    public void setExamForm(ExamForm examForm) {
        this.examForm = examForm;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Transient
    public File getFileHandle() {
        return new File(this.filePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        return id != null ? id.equals(document.id) : document.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Document{" +
                "lecture=" + lecture +
                ", lecturer=" + lecturer +
                ", examType=" + examType +
                ", examForm=" + examForm +
                ", semester='" + semester + '\'' +
                ", key='" + name + '\'' +
                '}';
    }
}
