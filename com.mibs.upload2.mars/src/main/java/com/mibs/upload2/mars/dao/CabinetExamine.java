package com.mibs.upload2.mars.dao;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CabinetExamine {

	private String email;
	protected String encode;
	
	public String getEmailDecodeBase64() {
		return email;
	}
	
	public String getEmail() {
		return new String(Base64.decodeBase64(email));
	}
	public String getEncode() {
		return encode;
	}
	public void setEmail(String e) {
		email = e;
	}
	public void setEncode(String e) {
		encode = e;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
