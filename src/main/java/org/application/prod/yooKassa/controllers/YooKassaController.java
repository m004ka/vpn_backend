package org.application.prod.yooKassa.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/kassa")
public class YooKassaController {

    @PostMapping("/newResponse")
    public String newCallbackKassa(){


        return null;
    }



}
