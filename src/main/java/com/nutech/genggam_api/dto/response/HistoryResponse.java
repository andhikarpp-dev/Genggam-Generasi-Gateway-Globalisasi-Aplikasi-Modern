package com.nutech.genggam_api.dto.response;

import java.util.List;

public class HistoryResponse {
    private Integer offset;
    private Integer limit;
    private List<HistoryItemResponse> records;

    public HistoryResponse() {}
    public HistoryResponse(Integer offset, Integer limit, List<HistoryItemResponse> records) {
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
    public List<HistoryItemResponse> getRecords() { return records; }
    public void setRecords(List<HistoryItemResponse> records) { this.records = records; }
}
