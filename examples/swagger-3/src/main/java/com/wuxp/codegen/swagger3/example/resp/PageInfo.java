package com.wuxp.codegen.swagger3.example.resp;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {

    private List<T> records;

    private Integer queryPage;

    private Integer querySize;

    public List<T> getRecords() {
        return records;
    }
}
