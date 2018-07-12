package com.mibs.upload2.mars.controllers;


import static com.mibs.upload2.mars.utils.MUtils.AYEAR;
import static com.mibs.upload2.mars.utils.MUtils.Password;
import static com.mibs.upload2.mars.utils.MUtils.UniqueID;
import static com.mibs.upload2.mars.utils.MUtils.regDate;
import static com.mibs.upload2.mars.utils.Messages.fromMail;
import static com.mibs.upload2.mars.utils.Messages.sexM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;


import com.mibs.upload2.mars.config.AppConfig;
import com.mibs.upload2.mars.dao.CabinetBuild;
import com.mibs.upload2.mars.entity.AddNewConclusionException;
import com.mibs.upload2.mars.entity.Conclusion;
import com.mibs.upload2.mars.entity.CreateLocalDicomDirException;
import com.mibs.upload2.mars.entity.Exploration;
import com.mibs.upload2.mars.entity.Payments;
import com.mibs.upload2.mars.entity.ReadingRemoteDicomDirException;
import com.mibs.upload2.mars.entity.ReadingRemoteDicomUrlException;
import com.mibs.upload2.mars.entity.RemotePaths;
import com.mibs.upload2.mars.entity.Request;
import com.mibs.upload2.mars.entity.Users;
import com.mibs.upload2.mars.exceptions.ErrorCreateProfileException;
import com.mibs.upload2.mars.exceptions.ErrorDicomParsingException;
import com.mibs.upload2.mars.exceptions.FolderNotFoundException;
import com.mibs.upload2.mars.exceptions.RemotePathNotFoundException;
import com.mibs.upload2.mars.repos.ConclusionRepository;
import com.mibs.upload2.mars.repos.ExplorationRepository;
import com.mibs.upload2.mars.repos.ImagesRepository;
import com.mibs.upload2.mars.repos.InvitationsRepository;
import com.mibs.upload2.mars.repos.JournalRepository;
import com.mibs.upload2.mars.repos.PaymentsRepository;
import com.mibs.upload2.mars.repos.RemotePathsRepository;
import com.mibs.upload2.mars.repos.RequestRepository;
import com.mibs.upload2.mars.repos.UsersRepository;
import com.mibs.upload2.mars.service.ExplorationUniqueName;

import com.mibs.upload2.mars.utils.MailAgent;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

abstract class AbstractController {
	static Logger logger = LoggerFactory.getLogger(AbstractController.class);
	private Locale locale = Locale.getDefault();
	
	protected ExplorationUniqueName explorationUniqueName;
	@Autowired 
	protected UsersRepository usersRepository;
	@Autowired
	protected AppConfig appConfig;
	@Autowired
	private ImagesRepository imagesRepository;
	@Autowired
	public ConclusionRepository conclusionRepository;
	@Autowired 
	protected ExplorationRepository explorationRepository;
	@Autowired
	protected RemotePathsRepository remotePathsRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	protected PaymentsRepository paymentsRepository;
	@Autowired
	protected InvitationsRepository invitationsRepository;
	@Autowired
	protected MessageSource messageSource;
	@Autowired
	protected JournalRepository journalRepository;
	
	protected void pack(String sourceDirPath, String zipFilePath) {
		try {
			ZipFile zipFile = new ZipFile(zipFilePath);
			ArrayList<File> fs = new ArrayList<>();
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setRootFolderInZip("Documents/");
			Path pp = Paths.get(sourceDirPath);
		    try {
				Files.walk(pp)
				      .filter(path -> !Files.isDirectory(path))
				      .forEach(path -> {
				    	  fs.add( new File(path.toString()));
				      });
			} catch (IOException e) {
				logger.error("Error while adding files to zip list with native message: " + e.getMessage());
			}
		    zipFile.addFiles(fs, parameters);
		    fs.forEach(f->{
		    	try {
					Files.delete( f.toPath() );
				} catch (IOException e) {
					logger.error("Error while deleting files from zip list with native message: " + e.getMessage());
				}
		    });
		} catch (ZipException e) {
			logger.error("Error while creating Zip object with native message: " + e.getMessage());
		}
	}
	private void addPayment(Users user, Request request ) {
		try {
			Float sum = Float.parseFloat( request.getCount() );
			Payments payments = new Payments();
			payments.setUserid(user.getId() );
			payments.setPaiddate( regDate() );
			payments.setPaidsum( sum );
			payments.setPaidtill( AYEAR + regDate()  );
			paymentsRepository.save(payments);
		}catch(Exception e) {
			logger.error("Error while setting payment value with message: " + e.getMessage());
		}
		 
	}
	private Users addUser( CabinetBuild cabinet ) {
		
		Users user = usersRepository.findByEmail( cabinet.getEmail() );
		if (user != null) {
			return user;
		}else {
			//Float sum = Float.parseFloat( request.getCount() );
			Users newUsers = new Users();
			newUsers.setLogin( UniqueID() );
			newUsers.setPasswd(  Password() );
			newUsers.setEmail( cabinet.getEmail());
			newUsers.setFirstname(cabinet.getFirst());
			newUsers.setLastname(cabinet.getParent());
			newUsers.setSurname(cabinet.getFamily());
			newUsers.setRoles("PATIENT");
			newUsers.setRegdate( regDate() );
			usersRepository.save(newUsers);
		}
		return addUser( cabinet );
    }
/*	protected void createNewProfile( CabinetBuild cabinet )  {
		
		String subject =  messageSource.getMessage("mail.template.subject", null, locale);
		explorationUniqueName = new ExplorationUniqueName();
		String s1 = cabinet.getPath().replaceAll("\\\\", "/");
		//Path s2 = Paths.get(s1);
		//String dstFoldername = s2.getName(s2.getNameCount() - 1).toString();
		//String dstFoldername = explorationUniqueName.getUniqueName();
		
		String serPath = appConfig.getSerializedPath() + "/" + cabinet.getUid();;
		String dicomPath;
		try {
			try {
				dicomPath = copyRemoteDICOMToLocalDir( cabinet.getPath());
				Dcm2Img dcm2Img = new Dcm2Img();
				Users user =  addUser( cabinet );
				Exploration exploration = addExploration(cabinet, user);
				try {
					addConclusions(cabinet, exploration );
					dcm2Img.initImageWriter("JPEG","jpeg", null, null,null);
					try {
						dcm2Img.CCCatch(dicomPath, serPath, exploration, imagesRepository);
						pack(dicomPath, dicomPath + "/" + cabinet.getUid() + ".zip");
						
						String text = messageSource.getMessage("mail.template.textM", null, locale) + "  " +  user.getFirstname() + "  " + user.getLastname()  + "!";
						String[] params = { user.getFirstname(), user.getEmail(), user.getPasswd() };
						String template = messageSource.getMessage("mail.template.temp3", params, locale);
						try {
							MailAgent.sendMail(fromMail, user.getEmail(), appConfig.getMaiSmtpHost(),  subject, text, template);
						} catch (MessagingException e) {
						
							logger.error("Error: " + e.getMessage() + ".  Email to :" + user.getEmail() +" has not been sent! " );
						}
					
					} catch (ErrorDicomParsingException e1) {
						logger.error("Error Dicom Parsing Exception: "  + e1.getMessage());
					}
				} catch (AddNewConclusionException e2) {
					
					logger.error("Error adding conclusion: "  + e2.getMessage());
				}
				
			} catch (RemotePathNotFoundException e3) {
			
				e3.printStackTrace();
			}
		
		} catch (CreateLocalDicomDirException | ReadingRemoteDicomDirException
				| ReadingRemoteDicomUrlException | IOException e1) {
			
			logger.error("Error while copying remote DICOM to local directory with message: "  +  e1.getMessage());
		}
	}
*/	
	protected void addConclusions(CabinetBuild cabinet, Exploration exploration) throws AddNewConclusionException {
		
		List<Conclusion> Cons =  conclusionRepository.findByExplorationid( exploration.getId());
		if (Cons.size() > 0) {
			for(Conclusion co : Cons) {
				conclusionRepository.delete(co);
			}
		}
		for(int i=0; i < cabinet.getConclusions().size(); i++) {
			Conclusion conclusion = new Conclusion();
			conclusion.setExplorationid( exploration.getId() );
			byte[] byteArray;
			try {
				byteArray = javax.xml.bind.DatatypeConverter.parseBase64Binary(new String(cabinet.getConclusions().get(i).getContent().getBytes()));
				conclusion.setConclusionfile( byteArray );
				conclusion.setFilename(Paths.get( cabinet.getConclusions().get(i).getName() ).getFileName().toString());
				conclusionRepository.save( conclusion );
			} catch (Exception e) {
				throw new AddNewConclusionException("Error adding new conclusion with message: " + e.getMessage());
			}
		}
		
	}

	private Exploration addExploration(CabinetBuild cabinet, Users users) {
		
		String s1 = cabinet.getPath().replaceAll("\\\\", "/");
		Path s2 = Paths.get(s1);
		String serPath = appConfig.getSerializedPath() + "/" + s2.getName(s2.getNameCount() - 1);
		Exploration exploration = new Exploration();
		exploration.setUsersId( users.getId());
		exploration.setDate(regDate());
		exploration.setUniqueid( cabinet.getUid() );
		exploration.setExplname( cabinet.getStudyname() );
		//exploration.setDicomname( s2.getName(s2.getNameCount() - 1).toString() );
		exploration.setDicomname( explorationUniqueName.getUniqueName() );
		explorationRepository.save(exploration);
		return exploration;
		
	}
	protected Exploration addExploration(ExplorationUniqueName unique, Users users,  String explorationName) {
		
		Exploration exploration = new Exploration();
		exploration.setUsersId( users.getId());
		exploration.setDate(regDate());
		exploration.setUniqueid( unique.getUniqueName() );
		exploration.setExplname( explorationName );
		exploration.setDicomname( unique.getUniqueName() );
		explorationRepository.save(exploration);
		return exploration;
		
	}
	

	private String copyRemoteDICOMToLocalDir(String path) throws RemotePathNotFoundException, CreateLocalDicomDirException, ReadingRemoteDicomDirException, ReadingRemoteDicomUrlException, IOException {
		String resultPath = null;
		String s1 = path.replaceAll("\\\\", "/");
		Path s2 = Paths.get(s1);
		String ipAddress = s2.getName(0).toString();
		String dirName = s2.getName(1).toString();
		RemotePaths remotePaths = remotePathsRepository.findByIpaddressAndDirname(ipAddress, dirName);

		if (remotePaths == null)  throw new RemotePathNotFoundException("ERROR_FIND_REMOTE_PATH") ;
		
		String login =remotePaths.getLogin(); 
		String password = remotePaths.getPasswd();
		String smbDirName = "smb://" + login + ":" + password +"@" + s1.substring(2);
		SmbFile sfile;
		try {
			sfile = new SmbFile( smbDirName );
			SmbFile[] files;
			try {
				files = sfile.listFiles();
				resultPath = appConfig.getStoragePath() + "/" + explorationUniqueName.getUniqueName();
				File destDir = new File( resultPath );
				if (!destDir.exists()) {
					if (!destDir.mkdir()) throw new CreateLocalDicomDirException("ERROR_CREATING_LOCAL_DICOM_DIR") ;
				}
				for(SmbFile f : files) {
					SmbFile dstfiles = new SmbFile( smbDirName + f.getName() );
					InputStream input = null;;
					OutputStream output = null;
					input = dstfiles.getInputStream();
					output = new FileOutputStream(resultPath + "/" + f.getName() );
					IOUtils.copy(input, output);
					input.close();
					output.close();
					
				}
			} catch (SmbException e) {
				throw new ReadingRemoteDicomDirException("ERROR_READING_REMOUTE_DICOM_DIRECTORY");
			}
		} catch (MalformedURLException e) {
			throw new ReadingRemoteDicomUrlException("ERROR_READING_REMOUTE_DICOM_URL");
		}
		return resultPath;
	}
	

	
}
