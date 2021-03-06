package in.clouthink.synergy.sms.history.domain.model;

import in.clouthink.synergy.shared.domain.model.StringIdentifier;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author dz
 */
@Document(collection = "SmsHistories")
public class SmsHistory extends StringIdentifier {

	public enum SmsStatus {
		SENT, FAILED
	}

	private String provider;

	private String telephone;

	private String message;

	private Date createdAt;

	private String category;

	private SmsStatus status;

	private String failureReason;

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public SmsStatus getStatus() {
		return status;
	}

	public void setStatus(SmsStatus status) {
		this.status = status;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
}
