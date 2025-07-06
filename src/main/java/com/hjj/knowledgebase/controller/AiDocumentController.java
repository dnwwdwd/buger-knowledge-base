package com.hjj.knowledgebase.controller;

import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.model.entity.AiDocument;
import com.hjj.knowledgebase.service.AiDocumentService;
import com.hjj.knowledgebase.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document")
public class AiDocumentController {

    @Resource
    private AiDocumentService aiDocumentService;

    @Resource
    private UserService userService;

    @GetMapping("/list/my")
    public BaseResponse<List<AiDocument>> listMy(HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        List<AiDocument> aiDocuments = aiDocumentService.lambdaQuery()
                .eq(AiDocument::getCreateBy, userId)
                .list();
        return ResultUtils.success(aiDocuments);
    }



}
