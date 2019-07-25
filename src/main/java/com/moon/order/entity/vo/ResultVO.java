package com.moon.order.entity.vo;

import lombok.Getter;

@Getter
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;


    private ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultVO<T> resultVO(Integer code, String msg, T data){
        return new ResultVO<T>(code,msg,data);
    }
}
