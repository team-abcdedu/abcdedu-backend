package com.abcdedu_backend.utils.exportable;
import lombok.Getter;

import java.util.List;
@Getter
public class ExcelData implements ExportDataProvider {
    private final String workbookName;
    private final List<String> headerName;

    private final List<List<Object>> rowData;

    private final String responseContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final String responseHeaderName = "Content-Disposition";

    private final String responseHeaderValue;

    public ExcelData(final String workbookName, final List<String> headerName, final List<List<Object>> rowData) {
        this.workbookName = workbookName;
        this.headerName = headerName;
        this.rowData = rowData;
        this.responseHeaderValue = "attachment;filename=" + workbookName + ".xlsx";
    }
}
