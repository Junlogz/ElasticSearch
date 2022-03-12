package com.zjl.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/10 20:20
 */
public class IndexSearch {

    TransportClient client;

    @Before
    public void init() throws Exception{
        // 1. 配置
        Settings settings = Settings.builder().put("cluster.name", "my-elasticsearch").build();
        // 2. 客户端
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("124.222.211.143"), 9300));

    }

    @Test
    public void testQueryByTerm() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title","今日");

        // 执行查询
        search(queryBuilder);
    }

    @Test
    public void testQueryByQueryString() throws Exception{
        // QueryString可以查出进入今日 而term查不出
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("进入今日").defaultField("title");

        // 执行查询
        search(queryBuilder);

    }

    @Test
    public void testQueryByMatchQuery() throws Exception{
        // Match可以查出进入今日 而term查不出
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title","进入今日");
        search(queryBuilder);

    }

    @Test
    public void testQueryById() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("2","3");
        search(queryBuilder);

    }

    @Test
    public void testQueryByMatchAll() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // 执行查询
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(5)
                .get();

        // 查询结果
        SearchHits searchHits = searchResponse.getHits();
        // 取查询结果的总记录数
        System.out.println("查询结果总记录数" + searchHits.getTotalHits());
        // 查询结果列表
        Iterator<SearchHit> iterator = searchHits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            // 打印文档对象 以json格式输出
            System.out.println(searchHit.getSourceAsString());
//            System.out.println("-------文档属性");
//            Map<String, Object> document = searchHit.getSource();
//            System.out.println(document.get("id"));
//            System.out.println(document.get("title"));
//            System.out.println(document.get("content"));
        }

        client.close();
    }

    @Test
    public void testQueryByHighlight() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "今日");

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        // 执行查询
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .highlighter(highlightBuilder)
                .get();

        // 查询结果
        SearchHits searchHits = searchResponse.getHits();
        // 取查询结果的总记录数
        System.out.println("查询结果总记录数" + searchHits.getTotalHits());
        // 查询结果列表
        Iterator<SearchHit> iterator = searchHits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            // 打印文档对象 以json格式输出
            System.out.println(searchHit.getSourceAsString());
            System.out.println("-------高亮结果");
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry :
                    highlightFields.entrySet()) {
                System.out.println(entry.getKey() + ":\t" + Arrays.toString(entry.getValue().fragments()));
            }
        }
        client.close();
    }

    private void search(QueryBuilder queryBuilder) {
        // 执行查询
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .get();

        // 查询结果
        SearchHits searchHits = searchResponse.getHits();
        // 取查询结果的总记录数
        System.out.println("查询结果总记录数" + searchHits.getTotalHits());
        // 查询结果列表
        Iterator<SearchHit> iterator = searchHits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            // 打印文档对象 以json格式输出
            System.out.println(searchHit.getSourceAsString());
            System.out.println("-------文档属性");
            Map<String, Object> document = searchHit.getSource();
            System.out.println(document.get("id"));
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
        }
        client.close();
    }
}
