package com.zjl.esspringesdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/11 13:40
 */
@Document(indexName = "car_index", type = "car")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Id
    @Field(type = FieldType.Long, store = true)
    private Long id;
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart")
    private String name;
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", fielddata = true)
    private String color;
    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", fielddata = true)
    private String brand;
    @Field(type = FieldType.Double, store = true)
    private Double price;

}
