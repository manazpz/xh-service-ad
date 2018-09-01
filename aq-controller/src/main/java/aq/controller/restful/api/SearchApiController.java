package aq.controller.restful.api;

import aq.service.search.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@Controller
@RequestMapping("/api/search")
public class SearchApiController extends aq.controller.restful.System {

    @Resource
    protected SearchService searchService;



}
