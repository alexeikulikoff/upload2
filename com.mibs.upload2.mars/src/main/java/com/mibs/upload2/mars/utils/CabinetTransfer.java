package com.mibs.upload2.mars.utils;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CabinetTransfer {

	private String first;
	private String parent;
	private String family;
	private String email;
	private String uid;
	private String studyName;
	private String path;
	private String isPaid;
	private String code;
	private List<Conclusion> conclusions;
	
	public CabinetTransfer() {}

	public void setConclusion(List<Conclusion> c) {
		conclusions = c;
	}
	public List<Conclusion> getConclusions(){
		return conclusions;
	}
	public String getFirst() {
		return first;
	}
	public String getParent() {
		return parent;
	}
	public String getFamily() {
		return family;
	}
	public String getEmail() {
		return email;
	}
	public String getUid() {
		return uid;
	}
	public String getStudyName() {
		return studyName;
	}
	public String getPath() {
		return path;
	}
	public String getIsPaid() {
		return isPaid;
	}
	public String getCode() {
		return code;
	}
	public void setFirst(String s) {
		first = s;
	}
	public void setParent(String s) {
		parent = s;
	}
	public void setFamily(String s) {
		family = s;
	}
	public void setEmail(String s) {
		email = s;
	}
	public void setUid(String s) {
		uid = s;
	}
	public void setStudyName(String s) {
		studyName = s;
	}
	public void setPath(String s) {
		path = s;
	}
	public void setIsPaid(String s) {
		isPaid = s;
	}
	public void setCode(String s) {
		code = s;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
