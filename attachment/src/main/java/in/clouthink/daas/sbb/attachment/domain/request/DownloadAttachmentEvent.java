package in.clouthink.daas.sbb.attachment.domain.request;


import in.clouthink.daas.sbb.account.domain.model.SysUser;
import in.clouthink.daas.sbb.attachment.domain.model.Attachment;

/**
 * The download attachment event
 */
public interface DownloadAttachmentEvent {

	String EVENT_NAME = DownloadAttachmentEvent.class.getName();

	Attachment getAttachment();

	SysUser getUser();

}
