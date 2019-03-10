package com.geeks18.virtualserver.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

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

@RestController
@RequestMapping("/virtualServer/model")
public class ModelGeneratorController {

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

	private void importFile(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(path));

		Sheet sheet = workbook.getSheetAt(0);
		Set<String> attributeList = new HashSet<>();
		getAttributes(sheet, attributeList);
		//createJavaBeans(attributeList, sheet.getSheetName());

		workbook.close();

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
