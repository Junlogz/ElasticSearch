package com.zjl.esspringesdemo.dao;

import com.zjl.esspringesdemo.domain.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/11 11:09
 */
public interface ArticleDao extends ElasticsearchRepository<Article, Long> {

    public List<Article> findByTitle(String title);

    public List<Article> findByTitleOrContent(String title, String content);

    public List<Article> findByTitleOrContent(String title, String content, Pageable pageable);
}
