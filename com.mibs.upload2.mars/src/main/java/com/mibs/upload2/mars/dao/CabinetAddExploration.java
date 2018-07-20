/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibs.upload2.mars.dao;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author alex
 */
public class CabinetAddExploration  extends CabinetExamine {
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
    	return new String(Base64.decodeBase64(exploration));
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
