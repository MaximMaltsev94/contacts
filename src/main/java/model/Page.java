package model;

import java.util.List;

public class Page<T> extends GenericModel {
    private List<T> data;
    private int currentPage;
    private int totalRowCount;

    public Page() {
    }

    public Page(List<T> data, int currentPage, int totalRowCount) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalRowCount = totalRowCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }
}
