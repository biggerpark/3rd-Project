package com.green.jobdone.visitor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/visit")
    public String visit(HttpServletRequest request) {
        visitorService.incrementVisitor(request);
        return "현재 방문자 수: " + visitorService.getVisitorCount();
    }
}
