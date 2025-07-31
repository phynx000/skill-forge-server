package com.skillforge.skillforge_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultPaginationDTO {

    private Meta meta;
    private Object results;

    @Getter
    @Setter
    public static class Meta {
        private int page;
        private int pageSize;
        private int pages;
        private long totalItems;
}
}
