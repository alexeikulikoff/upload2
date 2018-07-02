package com.mibs.upload2.mars.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadController {

	 @RequestMapping("/test")
	 public @ResponseBody String test() {
	        return "test OK";
	  }
}
