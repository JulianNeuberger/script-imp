package de.fsmpi.model.print;

import de.fsmpi.model.user.User;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
public class PrintJob {

	@Id
	@Column
	@GeneratedValue
	protected Long id;

	@Column
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	protected DateTime createdDate;

	@Column
	@Enumerated(EnumType.STRING)
	protected PrintStatus status;

	@ManyToMany
	@JoinTable
	protected Collection<PrintJobDocument> documents;

	@ManyToOne
	protected User issuedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Collection<PrintJobDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(Collection<PrintJobDocument> documents) {
		this.documents = documents;
	}

	public PrintStatus getStatus() {
		return status;
	}

	public void setStatus(PrintStatus status) {
		this.status = status;
	}

	public User getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(User issuedBy) {
		this.issuedBy = issuedBy;
	}

	@Transient
	public boolean needsApproval() {
		return this.status == PrintStatus.APPROVAL;
	}

	@Transient
	public int getNumberOfPages() {
		int totalPages = 0;
		if(documents == null) {
			return 0;
		}
		for (PrintJobDocument document : documents) {
			Integer pages = document.getDocument().getPages();
			if(pages == null) {
				pages = 0;
			}
			Integer count = document.getCount();
			if(count == null) {
				count = 0;
			}
			totalPages += pages * count;
		}
		return totalPages;
	}
}
