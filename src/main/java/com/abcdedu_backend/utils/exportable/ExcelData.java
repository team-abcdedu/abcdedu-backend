package com.abcdedu_backend.utils.exportable;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class ExcelData implements ExportDataProvider {
    private final String workbookName;
    private final List<String> headerName;

    private final List<List<Object>> rowData;

}
