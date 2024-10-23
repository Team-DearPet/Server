package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;
}
