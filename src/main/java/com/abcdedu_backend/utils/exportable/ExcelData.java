package com.abcdedu_backend.utils.exportable;


import java.util.List;

public class ExcelData implements ExportDataProvider {
    private final String workbookName;
    private final List<String> headerName;

    private final List<List<Object>> rowData;

    public ExcelData(final String workbookName, final List<String> headerName, final List<List<Object>> rowData) {
        this.workbookName = workbookName;
        this.headerName = headerName;
        this.rowData = rowData;
    }

    public String getWorkbookName() {
        return workbookName;
    }

    public List<String> getHeaderName() {
        return headerName;
    }

    public List<List<Object>> getRowData() {
        return rowData;
    }
}
