package in.clouthink.daas.sbb.dashboard.rest.controller;

import in.clouthink.daas.sbb.account.domain.model.SysUser;
import in.clouthink.daas.sbb.audit.domain.model.AuditEvent;
import in.clouthink.daas.sbb.audit.support.request.AuditEventQueryParameter;
import in.clouthink.daas.sbb.dashboard.rest.support.AuditEventRestSupport;
import in.clouthink.daas.sbb.security.SecurityContexts;
import in.clouthink.daas.audit.annotation.Ignored;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * frontend用户操作日志
 *
 * @author dz
 */
@Ignored
@RestController
@RequestMapping("/api")
public class AppAuditRestController {

	@Autowired
	private AuditEventRestSupport auditEventRestSupport;

	@ApiOperation(value = "审计日志列表,支持分页,支持动态查询(按名称,分类查询)")
	@RequestMapping(value = "/appAuditEvents", method = RequestMethod.GET)
	public Page<AuditEvent> listAuditEventPage(AuditEventQueryParameter queryRequest) {
		queryRequest.setRealm("frontend");
		return auditEventRestSupport.listAuditEventPage(queryRequest);
	}

	@ApiOperation(value = "审计日志明细")
	@RequestMapping(value = "/appAuditEvents/{id}", method = RequestMethod.GET)
	public AuditEvent getAuditEventDetail(@PathVariable String id) {
		return auditEventRestSupport.getAuditEventDetail(id);
	}

	@ApiOperation(value = "删除日志-以天为单位")
	@RequestMapping(value = "/appAuditEvents/byDay/{day}", method = RequestMethod.DELETE)
	public void deleteAuditEventsByDay(@PathVariable Date day) {
		SysUser user = (SysUser) SecurityContexts.getContext().requireUser();
		auditEventRestSupport.deleteAuditEventsByDay("frontend", day, user);
	}

	@ApiOperation(value = "删除日志-删除指定日期（不包括）之前的所有数据")
	@RequestMapping(value = "/appAuditEvents/beforeDay/{day}", method = RequestMethod.DELETE)
	public void deleteAuditEventsBeforeDay(@PathVariable Date day) {
		SysUser user = (SysUser) SecurityContexts.getContext().requireUser();
		auditEventRestSupport.deleteAuditEventsBeforeDay("frontend", day, user);
	}

}
