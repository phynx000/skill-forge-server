package com.skillforge.skillforge_api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultPaginationDTO {

    private Meta meta;
    private Object results;
}
