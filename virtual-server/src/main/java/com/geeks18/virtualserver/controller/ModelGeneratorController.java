package com.geeks18.virtualserver.controller;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geeks18.virtualserver.controller.util.RuleServerUtil;
 
@RestController
@RequestMapping("/virtualServer/model")
public class ModelGeneratorController {

	@Autowired
	private RuleServerUtil ruleServerUtil;
	
	
	@RequestMapping(value = "upload", consumes = { MimeTypeUtils.ALL_VALUE })
	public String upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("template.drt");

			String templatePath = url.toURI().getPath();
			String path = templatePath.substring(0, templatePath.lastIndexOf("/")) + "/" + file.getOriginalFilename();
			ruleServerUtil.saveFile(file.getInputStream(), path);
			ruleServerUtil.importFile(path);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getOriginalFilename();
	}


	
}
