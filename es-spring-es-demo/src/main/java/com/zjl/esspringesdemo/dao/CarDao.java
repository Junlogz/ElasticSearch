package com.zjl.esspringesdemo.dao;

import com.zjl.esspringesdemo.domain.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/11 11:09
 */
public interface CarDao extends ElasticsearchRepository<Car, Long> {

}
