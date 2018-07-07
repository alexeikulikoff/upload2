package com.mibs.upload2.mars.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Conclusion {
	private String name;
	private String content;
	public void setName(String s) {
		name = s;
	}
	public void setContent(String s) {
		content = s;
	}
	public String getContent() {
		return content;
	}
	public String getName() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Conclusion)) return false;
		Conclusion con = (Conclusion) obj;
		return con.getName().equals(name);
	}
	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"content\":" + content +"\"}"   ;
	}
}
