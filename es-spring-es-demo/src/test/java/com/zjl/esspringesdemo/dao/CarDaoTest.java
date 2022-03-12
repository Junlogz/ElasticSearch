package com.zjl.esspringesdemo.dao;

import com.zjl.esspringesdemo.domain.Car;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.util.List;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/11 14:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CarDaoTest extends TestCase {

    @Autowired
    private CarDao carDao;

    @Test
    public void initIndex(){
        carDao.save(new Car(1l,"比亚迪A1","红色","比亚迪",50000d));
        carDao.save(new Car(2l,"比亚迪A2","白色","比亚迪",60000d));
        carDao.save(new Car(3l,"比亚迪A3","白色","比亚迪",70000d));
        carDao.save(new Car(4l,"比亚迪A4","红色","比亚迪",80000d));
        carDao.save(new Car(5l,"比亚迪A5","红色","比亚迪",90000d));

        carDao.save(new Car(6l,"宝马A1","红色","宝马",10000d));
        carDao.save(new Car(7l,"宝马A2","黑色","宝马",20000d));
        carDao.save(new Car(8l,"宝马A3","黑色","宝马",30000d));
        carDao.save(new Car(9l,"宝马A4","红色","宝马",40000d));
        carDao.save(new Car(10l,"宝马A5","红色","宝马",50000d));

        carDao.save(new Car(11l,"奥迪A1","红色","奥迪",50000d));
        carDao.save(new Car(12l,"奥迪A2","黑色","奥迪",60000d));
        carDao.save(new Car(13l,"奥迪A3","黑色","奥迪",70000d));
        carDao.save(new Car(14l,"奥迪A4","红色","奥迪",80000d));
        carDao.save(new Car(15l,"奥迪A5","红色","奥迪",90000d));
    }

    @Test
    public void testQuerySelfAggs(){
        // 查询条件的构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery());
        //排除所有的字段查询
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        //添加聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms("group_by_color").field("color"));
        //执行查询，把查询结果直接转为聚合page
        AggregatedPage<Car> aggPage = (AggregatedPage<Car>) carDao.search(queryBuilder.build());

        //从所有的聚合中获取对应名称的聚合
        StringTerms agg = (StringTerms) aggPage.getAggregation("group_by_color");

        //从聚合的结果中获取所有的桶信息
        List<StringTerms.Bucket> buckets = agg.getBuckets();

        for (StringTerms.Bucket bucket : buckets) {
            String color = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            System.out.println("color = " + color + "总数：" + docCount);
        }

    }

    @Test
    public void testQueryBySubAggs(){
        // 查询条件的构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery());
        //排除所有的字段查询
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        //添加聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms("group_by_color").field("color")
                .subAggregation(AggregationBuilders.avg("avg_price").field("price")));
        //执行查询，把查询结果直接转为聚合page
        AggregatedPage<Car> aggPage = (AggregatedPage<Car>) carDao.search(queryBuilder.build());

        //从所有的聚合中获取对应名称的聚合
        StringTerms agg = (StringTerms) aggPage.getAggregation("group_by_color");

        //从聚合的结果中获取所有的桶信息
        List<StringTerms.Bucket> buckets = agg.getBuckets();

        for (StringTerms.Bucket bucket : buckets) {
            String color = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            //取得内部聚合
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("avg_price");

            System.out.println("color = " + color + "总数：" + docCount + " 价格 = " + avg);
        }

    }

}