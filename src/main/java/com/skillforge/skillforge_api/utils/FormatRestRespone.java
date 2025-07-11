package com.skillforge.skillforge_api.utils;


import com.skillforge.skillforge_api.entity.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestRespone implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpServletResponse.getStatus();
        RestResponse<Object> restResponse = new RestResponse<Object>();

        restResponse.setStatusCode(status);

        //Nếu đã là RestResponse rồi thì không wrap nữa
        if (body instanceof RestResponse) {
            return body;
        }

        if (body instanceof String){
            return body;
        }
        if(status >= 400) {
            restResponse.setError("Call api Failed");
            restResponse.setMessage(body);
        } else {
            restResponse.setData(body);
            restResponse.setMessage("Call api Success");
        }

        return restResponse;
    }
}
