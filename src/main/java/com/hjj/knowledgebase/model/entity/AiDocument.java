package com.hjj.knowledgebase.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * AI知识库文档
 * @TableName ai_document
 */
@TableName(value ="ai_document")
@Data
public class AiDocument implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private Double size;

    /**
     * AI知识库ids
     */
    private String documentRagIds;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}