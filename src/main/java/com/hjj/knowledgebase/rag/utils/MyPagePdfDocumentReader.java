package com.hjj.knowledgebase.rag.utils;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MyPagePdfDocumentReader {

    private final MyTokenTextSplitter splitter;

    List<Document> loadDocuments(Resource resource) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> documents = pdfReader.read();
        List<Document> newDocuments = documents.stream().filter(document ->
                StringUtils.isNotBlank(document.getText())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(newDocuments)) {
            return new ArrayList<>();
        }
        newDocuments = splitter.splitCustomized(documents).stream().filter(document ->
                StringUtils.isNotBlank(document.getText())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(newDocuments)) {
            return documents;
        }
        return newDocuments;
    }

}