package com.igrowker.miniproject.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api_Response<T> {
    private T data;
    private String message;
    private int statusCode;
}
