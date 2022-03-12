package com.zjl.esspringesdemo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/11 10:59
 */
@Document(indexName = "zjl_blog", type = "article")
@Data
public class Article {
    @Id
    @Field(type = FieldType.Long, store = true)
    private long id;
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart")
    private String content;

}
