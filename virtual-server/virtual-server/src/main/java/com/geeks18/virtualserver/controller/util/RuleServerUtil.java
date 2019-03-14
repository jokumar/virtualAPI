package com.geeks18.virtualserver.controller.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.geeks18.virtualserver.constants.VirtualServerConstant;
import com.geeks18.virtualserver.drools.DroolsConfiguration;
import com.geeks18.virtualserver.drools.model.GenericPojoModel;
import com.geeks18.virtualserver.rule.dto.HttpDetails;
import com.geeks18.virtualserver.rule.dto.MainRuleDTO;
import com.geeks18.virtualserver.service.RuleServerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class RuleServerUtil {
	
	@Resource
	RuleServerService ruleServerService;
	
	@Resource 
	DroolsConfiguration droolsConfiguration;
	public void importFile(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook workbook = WorkbookFactory.create(new File(path));

		Sheet sheet = workbook.getSheetAt(0);

		List<String> requestAttributeKeyList = new ArrayList<>();
		List<String> responseAttributeKeyList = new ArrayList<>();
		
		List<HashMap<String, String>> requestListMap = new ArrayList<>();
		List<HashMap<String, String>> responseListmap = new ArrayList<>();
		
		List<String> whenList=new ArrayList<>();
		List<String> thenList=new ArrayList<>();
		
		HttpDetails httpDetails=new HttpDetails();
		
		getAttributes(sheet, requestAttributeKeyList, responseAttributeKeyList, requestListMap, responseListmap,whenList,thenList,httpDetails);
		
		populateObject(requestListMap, responseListmap);
		
		populateDatabase(whenList, thenList);
		
		httpDetails.setRequestName(sheet.getSheetName() + "Request");
		httpDetails.setResponseName(sheet.getSheetName() + "Response");
		httpDetails.setPackageName("com.geeks18.virtualserver.drools.model.");
		
		createController(httpDetails,sheet.getSheetName());
		
		createJavaBeans(requestAttributeKeyList, httpDetails.getRequestName());
		createJavaBeans(responseAttributeKeyList,httpDetails.getResponseName() );
		
		droolsConfiguration.createDrl(httpDetails);
		
		workbook.close();

	}

	private void createController(HttpDetails httpDetails,String name) {
		try {

			String source = VirtualServerConstant.SOURCE_FILE_CONTROLLER + name+"Controller" + VirtualServerConstant.SOURCE_FILE_EXT;
			String className=name+"Controller";
			File sourceFile = new File(source);
			FileWriter writer = new FileWriter(sourceFile);
			writer.write("//This is a Auto Generated Class \n");

			writer.write("package com.geeks18.rule.controller;\n");

			StringBuilder sourceBuilder = new StringBuilder();
			sourceBuilder.append("import org.springframework.web.bind.annotation.RequestMapping;  \n")
			.append("import org.springframework.web.bind.annotation.RestController;  \n")
			.append("import com.geeks18.rule.controller.AbstractRestController;  \n")
			.append("import ").append(httpDetails.getPackageName()).append(httpDetails.getRequestName()).append("; \n")
			.append("import ").append(httpDetails.getPackageName()).append(httpDetails.getResponseName()).append("; \n")
			.append("@RestController  \n")
			.append("@RequestMapping(\""+httpDetails.getApi()+"\") \n")
			.append("public class "+className+" extends AbstractRestController<"+httpDetails.getRequestName()+","+httpDetails.getResponseName()+">{ \n")
			.append("public ").append(className).append("(){\n")
			.append("super(new "+httpDetails.getResponseName()+"()); \n")
			.append("} \n")
			.append("} \n");
			
			writer.write(sourceBuilder.toString());
			
			
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void populateDatabase(List<String> whenList,
			List<String> thenList) {
		
		List<MainRuleDTO> mainRuleDtoList=new ArrayList<>();
	
		for(int i=0;i<whenList.size();i++){
			MainRuleDTO mainRuleDto=new MainRuleDTO();
			mainRuleDto.setRuleId(i);
			mainRuleDto.setRuleName(VirtualServerConstant.RULE_NAME);
			mainRuleDto.setRuleTemplate(VirtualServerConstant.RULE_TEMPLATE);
			mainRuleDto.setWhenRule(whenList.get(i));
			mainRuleDto.setThenRule(thenList.get(i));
			mainRuleDtoList.add(mainRuleDto);
		}
		ruleServerService.deleteAllRules();//TODO This is to be removed . 
		ruleServerService.createRules(mainRuleDtoList);
	
	}

	public void createJavaBeans(List<String> attributeList, String objectName) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void populateObject(List<HashMap<String, String>> requestListMap,
			List<HashMap<String, String>> responseListMap) {
		List<GenericPojoModel> gPMList = new ArrayList<GenericPojoModel>();

		// TODO this genericPOJO Model needs to be built for request and
		// response .

		for (HashMap<String, String> map : requestListMap) {

			for (Map.Entry<String, String> entry : map.entrySet()) {
				GenericPojoModel gpm = new GenericPojoModel();
				gpm.setKey(entry.getKey());
				gpm.setValue(entry.getValue());
				gPMList.add(gpm);
			}

		}

		// TODO Need to be modified
		Gson gsonBuilder = new GsonBuilder().create();
		String jsonFromJavaArrayList = gsonBuilder.toJson(gPMList);
		System.out.println(jsonFromJavaArrayList);
	}

	public void getAttributes(Sheet sheet, List<String> requestAttributeKeyList, List<String> responseAttributeKeyList,
			List<HashMap<String, String>> requestList, List<HashMap<String, String>> responseList,List<String> whenList,List<String> thenList,HttpDetails httpDetails) {
		DataFormatter dataFormatter = new DataFormatter();

		int requestAttributeCount = 0;
		int responseAttributeCount = 0;
		for (Row row : sheet) {
			HashMap<String, String> requestMap = new HashMap<>();
			HashMap<String, String> responseMap = new HashMap<>();
			StringBuilder whenBuilder=new StringBuilder();
			StringBuilder thenBuilder=new StringBuilder();
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_0 && cell.getColumnIndex() == VirtualServerConstant.EXCEL_COL_1){
					httpDetails.setApi(cellValue);
				}else if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_1 && cell.getColumnIndex() == VirtualServerConstant.EXCEL_COL_1){
					httpDetails.setMethodType(cellValue);
				}
				else if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_2 && cell.getColumnIndex() == VirtualServerConstant.EXCEL_COL_1){
					httpDetails.setRequestType(cellValue);
				}
				else if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_3 && cell.getColumnIndex() == VirtualServerConstant.EXCEL_COL_1){
					httpDetails.setResponseType(cellValue);
				}
				else if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_4) {
					if (VirtualServerConstant.EXCEL_REQUEST.equalsIgnoreCase(cellValue)) {
						requestAttributeCount++;
					} else if (VirtualServerConstant.EXCEL_RESPONSE.equalsIgnoreCase(cellValue)) {
						responseAttributeCount++;
					}
					continue;
				} else if (cell.getRowIndex() == VirtualServerConstant.EXCEL_ROW_5) {
					if (cell.getColumnIndex() < requestAttributeCount) {
						requestAttributeKeyList.add(cellValue);
					} else if (cell.getColumnIndex() < requestAttributeCount + responseAttributeCount) {
						responseAttributeKeyList.add(cellValue);
					}
					continue;
				} else {
					if (cell.getColumnIndex() < requestAttributeCount) {
						String key=requestAttributeKeyList.get(cell.getColumnIndex());
						requestMap.put(key, cellValue);
						whenBuilder.append(key).append(" == \"").append(cellValue).append("\" && ");
						
					} else if (cell.getColumnIndex() < requestAttributeCount + responseAttributeCount) {
						String key=responseAttributeKeyList.get(cell.getColumnIndex() - requestAttributeCount);
						responseMap.put(key,
								cellValue);
						thenBuilder.append("$y.set").append(StringUtils.capitalize(key)).append("(\"").append(cellValue).append("\");");
					}

				}
			}
			if (!requestMap.isEmpty()) {
				requestList.add(requestMap);
				responseList.add(responseMap);
				whenList.add(whenBuilder.toString().substring(0, whenBuilder.toString().lastIndexOf("&&")));
				thenList.add(thenBuilder.toString());
			}
		}
	}

	public void saveFile(InputStream stream, String path) throws IOException {

		OutputStream outputstream = new FileOutputStream(new File(path));
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = stream.read(bytes)) != -1) {
			outputstream.write(bytes, 0, read);
		}
		outputstream.flush();
		outputstream.close();

	}
	// TODO need to move to process-engine
	/*
	 * public void convertIncomingFrontEndRequest(String payload) {
	 * 
	 * try { JSONObject jsonObj = new JSONObject(payload); List<String>
	 * allJsonValues = new ArrayList<String>(); List<String> allJsonKeys = new
	 * ArrayList<String>();
	 * 
	 * Iterator<String> keys = jsonObj.keys(); while(keys.hasNext()) { String
	 * key = keys.next(); allJsonKeys.add(key); String value =
	 * (String)jsonObj.get(key); allJsonValues.add(value); }
	 * populateObject(allJsonValues, allJsonKeys);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * 
	 * }
	 */

}
