package com.mibs.upload2.mars.dao;

import org.apache.commons.codec.binary.Base64;

public class CabinetProlong extends CabinetExamine{

	private String prolongationtime;
	
	public String getProlongationtime() {
		
		return new String(Base64.decodeBase64(prolongationtime));
		
	}
	public String getProlongationtimeDecodeBase64() {
		
		return prolongationtime;
		
	}
	public void setProlongationtime(String i) {
		prolongationtime = i;
	}
}
