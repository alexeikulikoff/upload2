/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibs.upload2.mars.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author alex
 */
public class Conclusion {
	private String name;
	private String content;
    
    public void setName(String s){
        name = s;
    }
    public void setContent(String s){
        content = s;
    }
	public String getContent() {
		return content;
	}
	public String getName() {
		return new String(Base64.decodeBase64(name));
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Conclusion)) return false;
		Conclusion con = (Conclusion) obj;
		return con.getName().equals(name);
	}
	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"content\":\"" + content +"\"}"   ;
	}
}

