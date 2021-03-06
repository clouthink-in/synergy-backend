package in.clouthink.synergy.audit.rest.support;

import in.clouthink.synergy.account.domain.model.User;
import in.clouthink.synergy.audit.domain.model.AuthEvent;
import in.clouthink.synergy.audit.rest.param.AuthEventSearchParam;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 */
public interface AuthEventRestSupport {

	Page<AuthEvent> listAuthEventPage(AuthEventSearchParam queryRequest);

	AuthEvent getAuthEventDetail(String id);

	void deleteAuthEventsByDay(String realm, Date day, User user);

	void deleteAuthEventsBeforeDay(String realm, Date day, User user);
}
