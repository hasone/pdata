package com.cmcc.file.excel.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtil {
	/**
	 * 读取excel文件中的内容
	 * @param excelPath
	 * @param indexMap
	 * @return
	 */
	public static HashMap<String, Double> readExcel(String excelPath, Map<Integer, Integer> indexMap, int startLineNumber, int endLineNumber){

		HashMap<String, Double> dataMap = new HashMap<String, Double>();

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(excelPath);
			// 根据指定的文件输入流导入Excel从而产生Workbook对象(对应工作簿)
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
			// 获取Excel表单信息
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 遍历所有行
			for (Row row : hssfSheet) {
				//设置取值范围，从第X行到第Y行
				int rowNum = row.getRowNum() + 1;//下标从1开始，第一行、第二行。。。。
				if(rowNum >= startLineNumber && rowNum <= endLineNumber){
					for (Map.Entry<Integer, Integer> entry : indexMap.entrySet()) {
						String key = row.getCell(entry.getKey()).getStringCellValue().trim();
						if(StringUtils.isBlank(key)){
							continue;
						}
						Double value = row.getCell(entry.getValue()).getNumericCellValue();
						dataMap.put(key, value);
					}
				}

			}
			hssfWorkbook.close();
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return dataMap;

	}
	

	public static boolean updateExcel(String srcPath, String desPath, Map<Integer, Integer> columnMap, Map<String, Double> dataMap, int startLineNumber, int endLineNumber){
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		HSSFWorkbook hssfWorkbook = null;
		try {
			fileInputStream = new FileInputStream(srcPath);
			// 根据指定的文件输入流导入Excel从而产生Workbook对象(对应工作簿)
			hssfWorkbook = new HSSFWorkbook(fileInputStream);
			// 关闭源数据流
			fileInputStream.close();
			// 获取Excel表单信息
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 遍历
			for (Row row : hssfSheet) {
				//设置更新范围，从第X行到第Y行
				int rowNum = row.getRowNum() + 1;//下标从1开始，第一行、第二行。。。。
				if(rowNum >= startLineNumber && rowNum <= endLineNumber){
					for (Map.Entry<Integer, Integer> entry : columnMap.entrySet()){
						Cell cell = row.getCell(entry.getKey());
						String key = cell.getStringCellValue().trim();
						if(StringUtils.isBlank(key)){
							continue;
						}
						Double value = dataMap.get(key);
						if(value == null){
							value = new Double(0.0);
						}
						row.getCell(entry.getValue()).setCellValue(dataMap.get(key));
					}				
				}
				
			}
			//写入输出流
			fileOutputStream=new FileOutputStream(desPath);
			hssfWorkbook.write(fileOutputStream);
			hssfWorkbook.close();
			fileOutputStream.close();
			return true;			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String args[]){
		HashMap<Integer, Integer> keysMap = new HashMap<Integer, Integer>();
		keysMap.put(0, 1);
		HashMap<String, Double> dataMap = readExcel("D:\\1.xls", keysMap, 7, 39);
		updateExcel("D:\\2.xls", "D:\\3.xls", keysMap, dataMap, 7, 39);
		System.out.println(dataMap);
	}
}
