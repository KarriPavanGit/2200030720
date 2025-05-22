package com.question1.avgcal.avgcal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.question1.avgcal.avgcal.models.ResponsePayload;
import com.question1.avgcal.avgcal.services.NumberService;

@RestController
@RequestMapping("/numbers")
public class NumberController {

	@Autowired
    public NumberService numberservice;

    @GetMapping("/{numid}")
    public ResponsePayload getResponse(@PathVariable String numid) {
        return numberservice.processrequest(numid.toLowerCase());
    }
}

