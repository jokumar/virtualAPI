package com.geeks18.virtualserver.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;

@RestController
@RequestMapping("/virtualServer/model")
public class ModelGeneratorController {

	@Resource
	RuleServerService ruleService;
	
	@RequestMapping(value = "upload", consumes = { MimeTypeUtils.ALL_VALUE })
	public String upload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("template.drt");

			String templatePath = url.toURI().getPath();
			String path = templatePath.substring(0, templatePath.lastIndexOf("/")) + "/" + file.getOriginalFilename();
			saveFile(file.getInputStream(), path);
			importFile(path);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getOriginalFilename();
	}

/*	private void createRulesinDb(Set<String> attributeList) {
		StringBuilder thenBuilder=new StringBuilder
		attributeList.forEach(x->{
			
		});
		
		MainRuleDTO dto=new MainRuleDTO();
		ruleService.createOrUpdate(mainRuleDto)
		
	}*/

	private void importFile(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(path));

		Sheet sheet = workbook.getSheetAt(0);
		Set<String> attributeList = new HashSet<>();
		getAttributes(sheet, attributeList);
		createJavaBeans(attributeList, sheet.getSheetName());

	//	createRulesinDb(attributeList);
		workbook.close();

	}

	private void createJavaBeans(Set<String> attributeList, String objectName) {
		try {

			String source = VirtualServerConstant.SOURCE_FILE + objectName + VirtualServerConstant.SOURCE_FILE_EXT;

			File sourceFile = new File(source);
			FileWriter writer = new FileWriter(sourceFile);
			writer.write("//This is a Auto Generated Class \n");

			writer.write("package com.geeks18.virtualserver.drools.model;\n");

			StringBuilder attributeBuilder = new StringBuilder();
			StringBuilder getterBuilder = new StringBuilder();
			StringBuilder setterBuilder = new StringBuilder();
			attributeList.forEach(x -> {
				attributeBuilder.append(" private String " + x.toLowerCase() + "; \n");
				getterBuilder.append(" public String get" + StringUtils.capitalize(x) + "(){ \n" + "return	this."
						+ x.toLowerCase() + "; \n" + "} \n");
				setterBuilder.append(" public void set" + StringUtils.capitalize(x) + "(String var){ \n" + "	this."
						+ x.toLowerCase() + "=var; \n" + "} \n");
			});

			writer.write("public class " + objectName + " extends GenericRuleModel{ \n" + attributeBuilder.toString()
					+ getterBuilder.toString() + setterBuilder.toString() + " public void doit() { \n"
					+ "   System.out.println(\"Hello world\") ;\n" + " }\n" +

					"}");
			writer.flush();
			writer.close();

			compile(objectName, source);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void compile(String name, String source) {
		try {
			System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_191");

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
					Arrays.asList(new File("target/classes/com/geeks18/virtualserver/drools/model")));
			compiler.run(null, null, null, source);
			fileManager.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getAttributes(Sheet sheet, Set<String> attributeList) {
		DataFormatter dataFormatter = new DataFormatter();
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				if (row.getRowNum() == 1) {
					attributeList.add(cellValue);
				}

				System.out.print(cellValue + "\t");
			}
		}
	}

	private void saveFile(InputStream stream, String path) throws IOException {

		OutputStream outputstream = new FileOutputStream(new File(path));
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = stream.read(bytes)) != -1) {
			outputstream.write(bytes, 0, read);
		}
		outputstream.flush();
		outputstream.close();

	}
}
