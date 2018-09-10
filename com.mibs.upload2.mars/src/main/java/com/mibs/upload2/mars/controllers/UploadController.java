package com.mibs.upload2.mars.controllers;

import static com.mibs.upload2.mars.utils.MUtils.regDate;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import static com.mibs.upload2.mars.utils.MUtils.prolongDate;
import static com.mibs.upload2.mars.utils.MUtils.Password;
import static com.mibs.upload2.mars.utils.MUtils.UniqueID;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.mibs.upload2.mars.dao.CabinetAddExploration;
import com.mibs.upload2.mars.dao.CabinetBuild;
import com.mibs.upload2.mars.dao.CabinetExamine;
import com.mibs.upload2.mars.dao.CabinetProlong;
import com.mibs.upload2.mars.dao.Conclusion;
import com.mibs.upload2.mars.entity.Exploration;
import com.mibs.upload2.mars.entity.Payments;
import com.mibs.upload2.mars.entity.Users;



@RestController
public class UploadController extends AbstractController{

	private final String ADMIN_EMAIL = "storage@mcomtech.ru";
	
	private Locale locale = Locale.getDefault();
	
	 @RequestMapping("/test")
	 public  String testUpload( Model model ) {
		return "test OK";
	 }
	 
	 @RequestMapping(value= {"/mailtest"} ,method = {RequestMethod.GET})
	 public  String mailtest( @RequestParam(value="mailto", required = true)  String mailto ) {
		 String emailto = mailto;
		 Users user = usersRepository.findByEmail( emailto );
		 if (user == null) return "No user found for email: " + emailto ;
		 try {
				
			 String[] params = { user.getSurname() + "  " + user.getFirstname() + " " + user.getLastname(), user.getEmail(), user.getPasswd() };
			 String template_newCabinet = messageSource.getMessage("mail.template.newCabinet", params, locale);
			 String subject =  messageSource.getMessage("mail.template.subject", null, locale);
			 MailAgent.sendMail(appConfig.getMailFrom(), emailto, appConfig.getMaiSmtpHost(),  subject, "", template_newCabinet);
			
			 String[] admin_params = { user.getSurname() + "  " + user.getFirstname() + " " + user.getLastname(), user.getEmail() };
			 String template_admin_newCabinet = messageSource.getMessage("mail.template.admin.newCabinet", admin_params, locale);
			 MailAgent.sendMail(appConfig.getMailFrom(), emailto, appConfig.getMaiSmtpHost(),  subject, "", template_admin_newCabinet);
	
			 } catch (MessagingException e) {
				e.printStackTrace();
				logger.error("Email to :" + emailto +" has not been sent!" );
		}
		 
		return "email to " + emailto + ", SmtpHost: " + appConfig.getMaiSmtpHost()  + " from " + appConfig.getMailFrom();
	 }
	 
	 @RequestMapping("/cabinetExamine")
	 public  String cabinetExamine( @RequestBody CabinetExamine cabinet ) {
		 Users user = usersRepository.findByEmail( cabinet.getEmail() );
		 if (user == null )  return "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
		 
		 List<Payments> pms = paymentsRepository.findByUserid( user.getId() );
		
		 if (pms != null) {
			 
			 
				
			 byte[] s0 = null;
			 byte[] s1 = null;
			 byte[] s2 = null;
			// try {
				//s0 = user.getSurname().getBytes("windows-1251");
				//s1 = user.getFirstname().getBytes("windows-1251");
				//s2 = user.getLastname().getBytes("windows-1251");
				
				s0 = user.getSurname().getBytes();
				s1 = user.getFirstname().getBytes();
				s2 = user.getLastname().getBytes();
				
				
			//} catch (UnsupportedEncodingException e) {
				
				// s0 = user.getSurname().getBytes();
				// s1 = user.getFirstname().getBytes();
				// s2 = user.getLastname().getBytes();
				//logger.error("Error encoding " + e.getMessage());
			//}
			 
			 
		     return "CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64() + 
		    			    ":" + Base64.encodeBase64String( s0 ) +
							":" + Base64.encodeBase64String(s1) + 
							":" + Base64.encodeBase64String( s2 ) +
							":" + Base64.encodeBase64String(pms.get(pms.size() -1 ).getPaidtillDate().getBytes()); 
		 }
		 return "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
					
	  }
	 @RequestMapping("/cabinetProlong")
	 public String cabinetProlong( @RequestBody CabinetProlong cabinet ) {
		 Users user = usersRepository.findByEmail( cabinet.getEmail() );
		 if (user == null )  return "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
		 
		 List<Payments> pms = paymentsRepository.findByUserid( user.getId() );
		
		 Long lastRegdate =  (pms.size() > 0) ? pms.get(pms.size() -1 ).getPaidtill() : regDate();
		
		 
			
		 byte[] s0 = null;
		 byte[] s1 = null;
		 byte[] s2 = null;
		// try {
			//s0 = user.getSurname().getBytes("windows-1251");
			//s1 = user.getFirstname().getBytes("windows-1251");
			//s2 = user.getLastname().getBytes("windows-1251");
		//} catch (UnsupportedEncodingException e) {
			
			s0 = user.getSurname().getBytes();
			s1 = user.getFirstname().getBytes();
			s2 = user.getLastname().getBytes();
		//	logger.error("Error encoding " + e.getMessage());
		//}
		 
		 
		 if (regDate() < lastRegdate)  return "ERROR_CABINET_ALREADY_PROLONGED:" + cabinet.getEmailDecodeBase64()
		 											+ ":" + Base64.encodeBase64String(s0) +
		 											":" + Base64.encodeBase64String(s1) + 
		 											":" + Base64.encodeBase64String( s2) +
		 											":" + Base64.encodeBase64String(pms.get(pms.size() -1 ).getPaidtillDate().getBytes()); 
		 											; 

		 
		 Payments payment = new Payments();
		 payment.setUserid(user.getId());
		 payment.setPaiddate(regDate());
		 payment.setPaidsum(new Float(500));
		 payment.setPaidtill( prolongDate( cabinet.getProlongationtime()));
		 try {
			 Payments pm = paymentsRepository.save ( payment );
		
			 
			 return "CABINET_IS_PROLONGED:" + cabinet.getEmailDecodeBase64() + 
					 ":" +  Base64.encodeBase64String(s0) +
					 ":" + Base64.encodeBase64String(s1) + 
					 ":" + Base64.encodeBase64String( s2 ) + 
					 ":" + Base64.encodeBase64String(pm.getPaidtillDate().getBytes());
			 
		 }catch(Exception e) {
			
			 return "ERROR_CABINET_PROLONGATION:" + cabinet.getEmailDecodeBase64(); 
 
		 }
	     
	  }
	 
	 
	 @RequestMapping("/cabinetAddExploration")
	 public String cabinetAddExploration( @RequestBody CabinetAddExploration cabinet ) {
		 
		 Exploration exp  = explorationRepository.findByUniqueid(cabinet.getUid());
		
		 if (exp != null) return "ERROR_ADD_NEW_EXPLORATION:" + cabinet.getUidDecodeBase64(); 
		 
		 Users user = usersRepository.findByEmail(cabinet.getEmail());
		 
		 if (user == null) return  "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
		 
		 
		 Exploration exploration = new Exploration();
		 exploration.setDate(regDate());
		 exploration.setExplname(cabinet.getExploration());
		 exploration.setUniqueid(cabinet.getUid());
		 exploration.setUsersId(user.getId());
		 exploration.setRemotepath( cabinet.getPath() );
		 exploration.setDicomname(  UniqueID() );
		 try {
			 explorationRepository.save(exploration); 
			 return "EXPLORATION_SAVED:" + cabinet.getUidDecodeBase64(); 
		 }  catch (Exception e) {
			 return "ERROR_ADD_NEW_EXPLORATION:" + cabinet.getUidDecodeBase64(); 
		 }
	 }
	 
	 
	 @RequestMapping("/cabinetBuild")
	 public String cabinetBuild( @RequestBody CabinetBuild cabinet ) {
		
		 logger.info("Cabinet :" + cabinet );
		 Users user = usersRepository.findByEmail(cabinet.getEmail());
		 if (user != null) {
			 List<Payments> pms = paymentsRepository.findByUserid( user.getId() );
			 if ((pms != null) && (pms.size() > 0)) { 
			 
				 return "CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64() + 
				 		":" + Base64.encodeBase64String(user.getSurname().getBytes()) +
					":" + Base64.encodeBase64String(user.getFirstname().getBytes()) + 
					":" + Base64.encodeBase64String( user.getLastname().getBytes()) +
					":" + Base64.encodeBase64String(pms.get(pms.size() -1 ).getPaidtillDate().getBytes()); 
		 	}else {
		 		return "ERROR_UNKNOWN:" + cabinet.getEmailDecodeBase64()  ; 
		 	}
		 }	 
		user = new Users();
		user.setFirstname(cabinet.getFirst());
		user.setLastname( cabinet.getParent());
		user.setSurname( cabinet.getFamily());
		user.setEmail( cabinet.getEmail() );
		user.setLogin( cabinet.getEmail());
		user.setPasswd( Password() );
		user.setMailed(0L);
		user.setRegdate(regDate());
		user.setRoles("PATIENT"); 
			
		Users savedUser = usersRepository.save( user ); 
		Payments payment = new Payments();
		payment.setUserid(user.getId());
		payment.setPaiddate(regDate());
		payment.setPaidsum(new Float(500));
		payment.setPaidtill( prolongDate( cabinet.getProlongationtime()));
		paymentsRepository.save ( payment );	 
			 
		 Exploration exploration = new Exploration();
		 exploration.setDate(regDate());
		 exploration.setExplname(cabinet.getStudyname());
		 exploration.setUniqueid(cabinet.getUid());
		 exploration.setUsersId(savedUser.getId());
		 exploration.setRemotepath( cabinet.getPath() );
		 exploration.setDicomname(  UniqueID() );
		 try {
			 explorationRepository.save(exploration);
			 try {
				   
				 String[] params = { user.getSurname() + "  " + user.getFirstname() + " " + user.getLastname(), user.getEmail(), user.getPasswd() };
				 String template_newCabinet = messageSource.getMessage("mail.template.newCabinet", params, locale);
				 String subject =  messageSource.getMessage("mail.template.subject", null, locale);
				 MailAgent.sendMail(appConfig.getMailFrom(), user.getEmail(), appConfig.getMaiSmtpHost(),  subject, "", template_newCabinet);
				
				 String[] admin_params = { user.getSurname() + "  " + user.getFirstname() + " " + user.getLastname(), user.getEmail() };
				 String template_admin_newCabinet = messageSource.getMessage("mail.template.admin.newCabinet", admin_params, locale);
				 MailAgent.sendMail(appConfig.getMailFrom(), ADMIN_EMAIL, appConfig.getMaiSmtpHost(),  subject, "", template_admin_newCabinet);
		
				 } catch (MessagingException e) {
					//e.printStackTrace();
					logger.error("Email to :" + user.getEmail() +" has not been sent!" );
			}
		
		 }catch(DataIntegrityViolationException e) {
			
			 return "ERROR_ADD_NEW_EXPLORATION:" + cabinet.getUidDecodeBase64(); 
		 
		 } 
		 byte[] s0 = null;
		 byte[] s1 = null;
		 byte[] s2 = null;
		// try {
		//	s0 = user.getSurname().getBytes("windows-1251");
		//	s1 = user.getFirstname().getBytes("windows-1251");
		//	s2 = user.getLastname().getBytes("windows-1251");
		//} catch (UnsupportedEncodingException e) {
			
			s0 = user.getSurname().getBytes();
			s1 = user.getFirstname().getBytes();
			s2 = user.getLastname().getBytes();
			//logger.error("Error encoding " + e.getMessage());
		//}
		 return "CABINET_BUILD_IN_PROGRESS:" + cabinet.getEmailDecodeBase64() + 
		 									":" + Base64.encodeBase64String( s0 ) +
											":" + Base64.encodeBase64String( s1 ) + 
											":" + Base64.encodeBase64String(  s2 ) ;
	 
	  }
}

/*			 for(Conclusion concl : cabinet.getConclusions()) {
com.mibs.upload2.mars.entity.Conclusion conclusion = new com.mibs.upload2.mars.entity.Conclusion();
conclusion.setExplorationid( savedExploration.getId() );
conclusion.setFilename(concl.getName());
conclusion.setConclusionfile(Base64.decodeBase64(concl.getContent()));
try {
	 conclusionRepository.save(conclusion); 
	
}catch(Exception e1) {
	 return "ERROR_SAVING_CONCLUSION:" + concl.getName() ; 
}
}
*/				