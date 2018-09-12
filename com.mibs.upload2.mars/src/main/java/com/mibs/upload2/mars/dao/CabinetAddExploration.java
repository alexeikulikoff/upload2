/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibs.upload2.mars.dao;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alex
 */
public class CabinetAddExploration  extends CabinetExamine {
	static Logger logger = LoggerFactory.getLogger(CabinetBuild.class);
    private String path;
    private String exploration;
    private String uid;
    
    public void setUid(String u) {
    	uid = u;
    }
    public String getUid() {
    	return new String(Base64.decodeBase64(uid));
    }
    public String getUidDecodeBase64() {
    	return uid;
    }
    public void setExploration(String e) {
    	exploration = e;
    }
    public void setPath(String p) {
    	path = p;
    }
    public String getExplorationDecodeBase64(){
    	return exploration;
    }
    public String getExploration(){
    	try {
			return encode.equals("cp1251") ? new String(Base64.decodeBase64(exploration),"windows-1251") 
				 : encode.equals("utf-8")  ? new String(Base64.decodeBase64(exploration)) : "Unknown charset" ;
	   } catch (UnsupportedEncodingException e1) {
			logger.error("Error while encoding value" + exploration);
			return "Encode error";
		}
    }
    public String getPath(){
    	return new String(Base64.decodeBase64(path));
    }
    public String getPathDecodeBase64(){
    	return exploration;
       
    }
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
