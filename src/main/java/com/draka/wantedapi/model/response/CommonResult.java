package com.draka.wantedapi.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    private boolean success;
    private int status_code;
    private String msg;
}
