package com.zhou.gulimall.product.exception;


import com.zhou.common.exception.BizCodeErume;
import com.zhou.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有异常
 */
@ResponseBody
@Slf4j
@ControllerAdvice(basePackages = "com.zhou.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R hanleValidException(MethodArgumentNotValidException e){
        log.error("数据校验出现问题{},异常类型,{}", e.getMessage(),e.getClass());
        Map<String,String> errorMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(item ->{
            errorMap.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(BizCodeErume.VALUE_EXCEPTION.getCode(),BizCodeErume.VALUE_EXCEPTION.getMsg()).put("data", errorMap);
    }
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.error("错误：", throwable);
        return R.error(BizCodeErume.UNKNOW_EXCEPTION.getCode(),BizCodeErume.UNKNOW_EXCEPTION.getMsg());
    }
}
