package com.hjj.knowledgebase.rag.utils;

import com.hjj.knowledgebase.common.ErrorCode;
import com.hjj.knowledgebase.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class MyTikaDocumentReader {

    private final MyTokenTextSplitter textSplitter;

    /**
     * 解析并对文件进行分片
     *
     * @param resource
     * @return
     */
    public List<Document> loadAndSplitDocuments(Resource resource) {
        String filename;
        try {
            filename = resource.getFile().getName();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件写入失败");
        }
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        List<Document> documents = tikaDocumentReader.read();
        if (CollectionUtils.isEmpty(documents)) {
            log.error("文件：{} 读取失败", filename);
            return new ArrayList<>();
        }
        documents = textSplitter.splitCustomized(documents);
        if (CollectionUtils.isEmpty(documents)) {
            log.error("文件：{} 切片失败", filename);
            return tikaDocumentReader.read();
        }
        return documents;
    }

}