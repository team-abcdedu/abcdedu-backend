package com.abcdedu_backend.utils.exportable;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

/**
 * 엑셀 생성 작업
 *  : ExcelData에서 데이터를 받아 엑셀 워크북을 생성하고 반환한다.
 */
public class ExcelExporter implements Exportable {

    private final ExcelData excelData;

    public ExcelExporter(ExcelData excelData) {
        this.excelData = excelData;
    }

    @Override
    public Workbook generateTool() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(excelData.getWorkbookName());
        generateHeader(sheet);
        generateRowData(sheet);

        return workbook;
    }


    /**
     * Sheet 헤더 생성
     */
    private void generateHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        List<String> headerNames = excelData.getHeaderName();
        for (int i = 0; i < headerNames.size(); i++) {
            headerRow.createCell(i).setCellValue(headerNames.get(i));
            // 열 너비 자동조정
            sheet.autoSizeColumn(i);
        }
    }
    /**
     * Sheet 행 생성
     */
    private void generateRowData(Sheet sheet) {
        List<List<Object>> rowData = excelData.getRowData();
        for (int rowIndex = 0; rowIndex < rowData.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            List<Object> rowValues = rowData.get(rowIndex);
            for (int colIndex = 0; colIndex < rowValues.size(); colIndex++) {
                row.createCell(colIndex).setCellValue(String.valueOf(rowValues.get(colIndex)));
            }
        }
    }


    @Override
    public void export(HttpServletResponse response) {
        try (Workbook workbook = generateTool()) {
            response.setContentType(excelData.getResponseContentType());
            response.setHeader(excelData.getResponseHeaderName(), excelData.getResponseHeaderValue());
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
           throw new ApplicationException(ErrorCode.EXPORT_IO_ERROR);
        }
    }

}
