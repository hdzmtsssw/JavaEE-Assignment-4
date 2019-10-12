package io.github.medioqrity;

import java.util.List;

import io.github.medioqrity.Student;

public class Paginator {
    
    private int rowPerPage;
    private List<Student> data;

    public void setRowPerPage(int rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    public int getRowPerPage() {
        return rowPerPage;
    }
    
    public void setData(List<Student> data) {
        this.data = data;
    }

    public List<Student> getData(int currentPage) {
        if (data == null) return null;
        return data.subList(currentPage * rowPerPage, Math.min((currentPage + 1) * rowPerPage, data.size()));
    }

    public int getDataSize() {
        if (data == null) return 0;
        return data.size();
    }
}