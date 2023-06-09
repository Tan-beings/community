package com.tanquandan.blogsystem.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> objects;
    private int currentPage;
    private int pageSize;
    private int pageNumberListLength = 5;
    private long totalItems;
    private boolean showPrevious;
    private boolean showNext;
 
    private List<Integer> pageNumberList;

    public PaginationDTO(List<T> objects,int currentPage,int pageSize,long totalItems){
        this.objects = objects;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
    }

    public void setPagination(){
        this.pageNumberList = new ArrayList<>();
        int endPageNumber = (int) (this.totalItems/this.pageSize+(this.totalItems%this.pageSize>0?1:0));
        int startPageNumber = 1;

        // 1. 确定显示列表
        int i = -3;
        while(pageNumberList.size() < this.pageNumberListLength){
            if(this.currentPage + i > endPageNumber){
                break;
            }
            if(this.currentPage + i >0 ){
                this.pageNumberList.add(currentPage+i);
            }
            i++;
        }

        // 2. 确定是否显示下一个和上一个
        this.showNext = !this.pageNumberList.contains(endPageNumber);
        this.showPrevious = !this.pageNumberList.contains(startPageNumber);
    }

}
