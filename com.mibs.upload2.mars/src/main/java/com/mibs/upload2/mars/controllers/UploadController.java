package com.mibs.upload2.mars.controllers;

import static com.mibs.upload2.mars.utils.MUtils.regDate;

import java.util.List;

import static com.mibs.upload2.mars.utils.MUtils.prolongDate;
import static com.mibs.upload2.mars.utils.MUtils.Password;
import static com.mibs.upload2.mars.utils.MUtils.UniqueID;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mibs.upload2.mars.dao.CabinetBuild;
import com.mibs.upload2.mars.dao.CabinetExamine;
import com.mibs.upload2.mars.dao.CabinetProlong;
import com.mibs.upload2.mars.dao.Conclusion;
import com.mibs.upload2.mars.entity.Exploration;
import com.mibs.upload2.mars.entity.Payments;
import com.mibs.upload2.mars.entity.Users;



@RestController
public class UploadController extends AbstractController{

	 @RequestMapping("/test")
	 public  String testUpload( Model model ) {
		return "test passed";
	 }
	 
	 @RequestMapping("/cabinetExamine")
	 public  String cabinetExamine( @RequestBody CabinetExamine cabinet ) {
		 Users user = usersRepository.findByEmail( cabinet.getEmail() );
		 if (user == null )  return "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
	     return "CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
	  }
	 @RequestMapping("/cabinetProlong")
	 public String cabinetProlong( @RequestBody CabinetProlong cabinet ) {
		 Users user = usersRepository.findByEmail( cabinet.getEmail() );
		 if (user == null )  return "ERROR_CABINET_EXAMINE_USER_EXIST:" + cabinet.getEmailDecodeBase64();
		 
		 List<Payments> pms = paymentsRepository.findByUserid( user.getId() );
		
		 Long lastRegdate =  (pms.size() > 0) ? pms.get(pms.size() -1 ).getPaidtill() : regDate();
		
		 if (regDate() < lastRegdate)  return "ERROR_CABINET_ALREADY_PROLONGED:" + cabinet.getEmailDecodeBase64()
		 											+ " " +  Base64.encodeBase64String(user.getSurname().getBytes()) +
		 											"  " + Base64.encodeBase64String(user.getFirstname().getBytes()) + " " + Base64.encodeBase64String( user.getLastname().getBytes()); 

		 
		 Payments payment = new Payments();
		 payment.setUserid(user.getId());
		 payment.setPaiddate(regDate());
		 payment.setPaidsum(new Float(500));
		 payment.setPaidtill( prolongDate( cabinet.getProlongationtime()));
		 try {
			 Payments pm = paymentsRepository.save ( payment );
			
			 return "CABINET_IS_PROLONGED:" + cabinet.getEmailDecodeBase64() + " " +  Base64.encodeBase64String(user.getSurname().getBytes()) +
					 "  " + Base64.encodeBase64String(user.getFirstname().getBytes()) + " " + Base64.encodeBase64String( user.getLastname().getBytes()); 
		 }catch(Exception e) {
			
			 return "ERROR_CABINET_PROLONGATION: " + cabinet.getEmailDecodeBase64() + " " +  Base64.encodeBase64String(user.getSurname().getBytes()) +
					 "  " + Base64.encodeBase64String(user.getFirstname().getBytes()) + " " + Base64.encodeBase64String( user.getLastname().getBytes()); 
 
		 }
	     
	  }
	 @RequestMapping("/cabinetBuild")
	 public String cabinetBuild( @RequestBody CabinetBuild cabinet ) {
		 Users user = usersRepository.findByEmail(cabinet.getEmail());
		 if (user == null) {
			 user = new Users();
			 user.setFirstname(cabinet.getFirst());
			 user.setLastname( cabinet.getParent());
			 user.setSurname( cabinet.getFamily());
			 user.setEmail( cabinet.getEmail() );
			 user.setLogin( cabinet.getEmail());
			 user.setPasswd( Password() );
			 user.setIsEmailChecked(0);
			 user.setRegdate(regDate());
			 user.setRoles("PATIENT"); 
		 }
		 try {
			 Users savedUser = usersRepository.save( user ); 
			 Exploration exploration = new Exploration();
			 exploration.setDate(regDate());
			 exploration.setExplname(cabinet.getStudyname());
			 exploration.setUniqueid(cabinet.getUid());
			 exploration.setUsersId(savedUser.getId());
			 exploration.setRemotepath( cabinet.getPath() );
			 exploration.setDicomname(  UniqueID() );
			 try {
				 Exploration savedExploration = explorationRepository.save(exploration);
				 for(Conclusion concl : cabinet.getConclusions()) {
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
			 }catch(DataIntegrityViolationException e) {
				 return "ERROR_ADD_NEW_EXPLORATION:" + cabinet.getUidDecodeBase64(); 
			 }catch(Exception e1) {
				 return "ERROR_UNKNOWN:" + cabinet.getUidDecodeBase64() ;
			 }
		 }catch(Exception e ) {
			 return "ERROR_UNKNOWN:" + cabinet.getUidDecodeBase64() ; 
		 }
		 String res = cabinet.getEmailDecodeBase64() + " " + cabinet.getFamilyDecodeBase64() + " " + cabinet.getFirstDecodeBase64() + " " + cabinet.getParentDecodeBase64() ;
		 return "CABINET_BUILD_IN_PROGRESS:" + res ;
	  }
}
