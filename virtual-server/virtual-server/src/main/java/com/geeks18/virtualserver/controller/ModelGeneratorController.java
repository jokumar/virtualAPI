package com.geeks18.virtualserver.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
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
			executeBatchFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getOriginalFilename();
	}

	private void executeBatchFile() {
		Thread commandLineThread=new Thread(()->{
		try{
		//ProcessBuilder processBuilder=new ProcessBuilder("C:\\Users\\joykumar\\Documents\\My POC\\Virtualized Server\\virtual-server\\process-engine\\exec.bat")
		Process process=	Runtime.getRuntime().exec("cmd /c exec.bat",null,new File(VirtualServerConstant.SOURCE_FILE_BAT));
		 StringBuilder output = new StringBuilder();
		BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        System.out.println(output);
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            System.out.println(output);
            System.exit(0);
        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		});
		commandLineThread.setDaemon(true);
		commandLineThread.start();
	}
	

	
}
