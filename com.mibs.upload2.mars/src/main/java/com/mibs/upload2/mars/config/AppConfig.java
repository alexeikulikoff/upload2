package com.mibs.upload2.mars.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class AppConfig {
	@Value("${mars.storage_path}")
	private String storagePath;
	
	@Value("${mars.serialized_path}")
	private String serializedPath;
	
	@Value("${mars.mail_confirm_period}")
	private String maiConfirmPeriod;
	
	@Value("${mars.mail_smtp_host}")
	private String maiSmtpHost;
	
	@Value("${mars.mail_port}")
	private String mailPort;
	
	@Value("${mars.mail_from}")
	private String mailFrom;
	
	@Value("${server.address}")
	private String serverAddress;
	
	@Value("${server.port}")
	private String serverPort;
	
	@Value("${server.context-path}")
	private String contextPath;
	public String getStoragePath(){
		return storagePath;
	}
	public String getSerializedPath(){
		return serializedPath;
	}
	public void  setStoragePath(String s){
		storagePath = s;
	}
	public String getMaiConfirmPeriod(){
		return maiConfirmPeriod;
	}
	public String getMaiSmtpHost(){
		return maiSmtpHost;
	}
	public String getMailPort(){
		return mailPort;
	}
	public String getMailFrom(){
		return mailFrom;
	}
	public String getServerAddress(){
		return serverAddress;
	}
	public String getServerPort(){
		return serverPort;
	}
	public String getContextPath(){
		return contextPath;
	}
}
