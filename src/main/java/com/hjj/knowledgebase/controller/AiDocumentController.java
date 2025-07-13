package com.hjj.knowledgebase.controller;

import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.model.entity.AiDocument;
import com.hjj.knowledgebase.service.AiDocumentService;
import com.hjj.knowledgebase.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/document")
@Tag(name = "知识库文档", description = "知识库文档")
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

    @PostMapping("/upload")
    @Operation(description = "文件上传", summary = "文件上传")
    public BaseResponse<Boolean> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return ResultUtils.success(aiDocumentService.upload(file, request));
    }

    @PostMapping("/delete/{id}")
    @Operation(description = "删除文档", summary = "删除文档")
    public BaseResponse<Boolean> delete(@PathVariable("id") Long id) {
        return ResultUtils.success(aiDocumentService.delete(id));
    }


}
