package com.sari.system.api;


import com.sari.system.application.StaticPageService;
import com.sari.system.domain.StaticPage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pages")
public class StaticPageController {


    private final StaticPageService service;

    public StaticPageController(StaticPageService service) {
        this.service = service;
    }

    @GetMapping("/{code}")
    public StaticPage get(@PathVariable String code) {
        return service.get(code);
    }

    @PostMapping("/{code}")
    public StaticPage save(@PathVariable String code,
                           @RequestBody String content) {
        return service.save(code, content);
    }

}
