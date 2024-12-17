package com.abcdedu_backend.utils.exportable;


import jakarta.servlet.http.HttpServletResponse;

public interface Exportable {
    /**
     * 실제로 파일을 다운할 수 있게 파일로 export
     */
    void export(HttpServletResponse response, ExportDataProvider exportDataProvider);
}
