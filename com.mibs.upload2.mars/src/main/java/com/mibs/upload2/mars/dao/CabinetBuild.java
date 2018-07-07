package com.mibs.upload2.mars.dao;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CabinetBuild extends CabinetExamine {
	private String first;
	private String parent;
	private String family;
	
	private String uid;
	private String studyname;
	private String path;
	private String prolongationtime;
	private List<Conclusion> conclusions;

	public List<Conclusion> getConclusions(){
		return conclusions;
	}
	public void setConclusions(List<Conclusion> conclusions) {
		this.conclusions  = conclusions;
	}
	
	public String getProlongationtime() {
		return prolongationtime;
	}
	public void setProlongationtime(String s) {
		prolongationtime = s;
	}
	
	public String getFirstDecodeBase64() {
		return first;
	}
	public String getFirst() {
		return new String(Base64.decodeBase64(first));
	}
	public void setFirst(String s) {
		first = s;
	}

	public String getParent() {
		return new String(Base64.decodeBase64(parent));
	}
	public String getParentDecodeBase64() {
		return parent;
	}
	public void setParent(String s) {
		parent = s;
	}
	public String getFamilyDecodeBase64() {
		return family;
	}
	public String getFamily() {
		return new String(Base64.decodeBase64(family));
	}
	public void setFamily(String s) {
		family = s;
	}
	public String getUidDecodeBase64() {
		return uid;
	}
	public String getUid() {
		return new String(Base64.decodeBase64(uid) );
	}
	public void setUid(String s) {
		uid = s;
	}
	
	public String getStudynameDecodeBase64() {
		return studyname;
	}
	public String getStudyname() {
		return new String(Base64.decodeBase64(studyname));
	}
	public void setStudyname(String s) {
		studyname = s;
	}
	
	public String getPathDecodeBase64() {
		return path;
	}
	public String getPath() {
		return new String(Base64.decodeBase64(path));
	}
	public void setPath(String s) {
		path = s;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
