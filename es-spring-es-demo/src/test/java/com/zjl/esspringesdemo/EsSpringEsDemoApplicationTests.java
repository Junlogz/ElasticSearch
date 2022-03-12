package com.zjl.esspringesdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjl.esspringesdemo.dao.ArticleDao;
import com.zjl.esspringesdemo.domain.Article;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsSpringEsDemoApplicationTests {

    @Autowired
    private ArticleDao dao;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void createIndex() {
        template.createIndex(Article.class);
    }

    @Test
    public void testAddDocument3() throws Exception{
        for (int i = 1; i < 20; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("今日解说" + i);
            article.setContent("今日解说电影血战钢锯岭" + i);
            dao.save(article);
        }
    }

    @Test
    public void findAll(){
        dao.findAll().forEach(System.out::println);
    }
}
