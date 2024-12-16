package com.abcdedu_backend.utils.exportable;


import jakarta.servlet.http.HttpServletResponse;

public interface Exportable {
    /**
     * file을 만들기 위해 준비하는 모든 행위 (엑셀로 치면 work북 생성 및 행, 열 설정)
     * @return 파일 tool (형식)
     */
    Object generateTool();// Todo 제네릭 이용해서 export 파일 종류 지정 하고 반환 값 특정하기

    /**
     * 실제로 파일을 다운할 수 있게 파일로 export
     */
    void export(HttpServletResponse response);
}
