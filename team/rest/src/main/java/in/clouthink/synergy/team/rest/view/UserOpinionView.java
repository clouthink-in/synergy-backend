package in.clouthink.synergy.team.rest.view;

import in.clouthink.synergy.team.domain.model.UserOpinion;

import java.util.Date;

/**
 */
public class UserOpinionView {

	public static UserOpinionView from(UserOpinion userOpinion) {
		if (userOpinion == null) {
			return null;
		}
		UserOpinionView result = new UserOpinionView();
		result.setId(userOpinion.getId());
		result.setContent(userOpinion.getContent());
		result.setCreatedById(userOpinion.getCreatedBy().getId());
		result.setCreatedByName(userOpinion.getCreatedBy().getUsername());
		result.setCreatedAt(userOpinion.getCreatedAt());
		return result;
	}

	private String id;

	private String content;

	private String createdById;

	private String createdByName;

	private Date createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
