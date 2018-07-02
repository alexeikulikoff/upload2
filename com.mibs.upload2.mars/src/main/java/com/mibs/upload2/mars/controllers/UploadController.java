package com.mibs.upload2.mars.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mibs.upload2.mars.utils.DataContainer;

@RestController
public class UploadController {

	 @RequestMapping("/testUpload")
	 public @ResponseBody String testUpload( @RequestBody DataContainer dc) {
		    System.out.println( dc );
	        return "test OK";
	  }
	 @RequestMapping("/uploader")
	 public  String index( Model model ) {
	        return "uploader";
	  }
}
