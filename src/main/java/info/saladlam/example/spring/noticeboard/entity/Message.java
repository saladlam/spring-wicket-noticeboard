package info.saladlam.example.spring.noticeboard.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

	private Long id;
	private LocalDateTime publishDate;
	private LocalDateTime removeDate;
	private String owner;
	private String description;
	private String approvedBy;
	private LocalDateTime approvedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDateTime publishDate) {
		this.publishDate = publishDate;
	}

	public LocalDateTime getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(LocalDateTime removeDate) {
		this.removeDate = removeDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Message)) return false;
		Message message = (Message) o;
		return id.equals(message.id) && publishDate.equals(message.publishDate) && Objects.equals(removeDate, message.removeDate) && owner.equals(message.owner) && description.equals(message.description) && Objects.equals(approvedBy, message.approvedBy) && Objects.equals(approvedDate, message.approvedDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, publishDate, removeDate, owner, description, approvedBy, approvedDate);
	}

}
