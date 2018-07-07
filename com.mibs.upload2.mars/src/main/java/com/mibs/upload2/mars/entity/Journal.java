package com.mibs.upload2.mars.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mibs.upload2.mars.dao.Conclusion;

@Entity
@Table(name = "journal")
public class Journal implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "sequence-generator"
    )
    @SequenceGenerator(
        name = "sequence-generator",
        sequenceName = "journal_id_seq"
    )

	@Column(name = "id")
	private Long id;
	@Column(name = "first")
	private String first;
	@Column(name = "parent")
	private String parent;
	@Column(name = "family")
	private String family;
	@Column(name = "email")
	private String email;
	@Column(name = "uid")
	private String uid;
	@Column(name = "studyname")
	private String studyname;
	@Column(name = "path")
	private String path;
	@Column(name = "curdate")
	private Long curdate;
	@Column(name = "prolongedtill")
	private Long prolongedtill;
	@Column(name = "flag")
	private int flag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long i) {
		id = i;
	}
	public Long getProlongedtill() {
		return prolongedtill;
	}
	public void setProlongedtill(Long s) {
		prolongedtill = s;
	}
	public Long getCurdate() {
		return curdate;
	}
	public void setCurdate(Long s) {
		curdate = s;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String s) {
		first = s;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String s) {
		parent = s;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String s) {
		family = s;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String s) {
		email = s;
	}
	public String getUid() {
		return (uid) ;
	}
	public void setUid(String s) {
		uid = s;
	}
	public String getStudyname() {
		return studyname;
	}
	public void setStudyname(String s) {
		studyname = s;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String s) {
		path = s;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int s) {
		flag = s;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
