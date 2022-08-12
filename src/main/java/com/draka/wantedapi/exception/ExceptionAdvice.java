package com.draka.wantedapi.exception;

import com.draka.wantedapi.model.response.CommonResult;
import com.draka.wantedapi.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(CompanyDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult companyDuplicateException(HttpServletRequest request, CompanyDuplicateException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("companyDuplicate.status_code")), getMessage("companyDuplicate.msg"));
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult companyNotFoundException(HttpServletRequest request, CompanyNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("companyNotFound.status_code")), getMessage("companyNotFound.msg"));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
