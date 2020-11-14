package org.la.ecom.msql.rest.controller;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.la.ecom.msql.model.MailFormat;
import org.la.ecom.msql.model.User;
import org.la.ecom.msql.service.MailFormatService;
import org.la.ecom.msql.service.UserService;
import org.la.ecom.mysql.api.AppConstants;
import org.la.ecom.mysql.api.dto.MailFormatDTO;
import org.la.ecom.mysql.api.dto.MailFormatEmailListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MailFormatService mailFormatService;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@GetMapping("/allMailsToSend/{mailSent}")
	public MailFormatEmailListDTO getAllMailsToSend(@PathVariable int mailSent){
		
		List<String> mailList = userService.getAllEmailsToSend(mailSent);
		MailFormatEmailListDTO mailFormatEmailListDTO = null;
		
		if(mailList!=null && !mailList.isEmpty()) {
			
			mailFormatEmailListDTO = new MailFormatEmailListDTO();
			MailFormat mailFormat = mailFormatService.findByMailTypeId(AppConstants.MAIL_SEND_ON_REGISTRATION);
			
			if(mailFormat!=null) {
				mailFormatEmailListDTO.setMailList(mailList);
				mailFormatEmailListDTO.setMailFormatDTO(mapper.map(mailFormat, MailFormatDTO.class));
			}
		}
		
		return mailFormatEmailListDTO;
	}
	
	@PutMapping("/updateMailSentStatusSent")
	public boolean updateMailSentStatusSent(@RequestBody List<String> mailList) {
		
		mailList.stream().forEach(mail -> {
			User user = userService.findByEmail(mail);
			user.setMailSentStatus(AppConstants.MAIL_SEND_ON_REGISTRATION_SUCCESS);
			userService.update(user);
		});
		return true;
	}
	
}
