package com.example.adminservice.controller.advice;

import com.example.adminservice.service.VisitCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class VisitCountControllerAdvice {

    private final VisitCountService service;

    @ModelAttribute("visitCount")
    public Long visitCount() {
        return service.visitCount();
    }
}
