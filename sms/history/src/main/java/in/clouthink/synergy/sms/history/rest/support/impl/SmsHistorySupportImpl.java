package in.clouthink.synergy.sms.history.rest.support.impl;

import in.clouthink.synergy.sms.history.domain.model.SmsHistory;
import in.clouthink.synergy.sms.history.domain.request.SmsHistorySearchRequest;
import in.clouthink.synergy.sms.history.rest.dto.SmsHistorySummary;
import in.clouthink.synergy.sms.history.rest.support.SmsHistorySupport;
import in.clouthink.synergy.sms.history.service.SmsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SmsHistorySupportImpl implements SmsHistorySupport {

	@Autowired
	private SmsHistoryService smsHistoryService;

	@Override
	public Page<SmsHistorySummary> findPage(SmsHistorySearchRequest request) {
		Page<SmsHistory> smsHistories = smsHistoryService.findPage(request);
		return new PageImpl<>(smsHistories.getContent()
										  .stream()
										  .map(SmsHistorySummary::from)
										  .collect(Collectors.toList()),
							  new PageRequest(request.getStart(), request.getLimit()),
							  smsHistories.getTotalElements());
	}
}
