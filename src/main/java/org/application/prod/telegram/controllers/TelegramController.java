package org.application.prod.telegram.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/telegram")
public class TelegramController {

    @GetMapping("getUserById")
    public String getUser(Long id){



        return null;
    }
}
