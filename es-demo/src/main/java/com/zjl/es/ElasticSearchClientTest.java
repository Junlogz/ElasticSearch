package com.zjl.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @author: JunLog
 * @Description: *
 * Date: 2022/3/10 17:11
 */
public class ElasticSearchClientTest{

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
    public void createIndex() throws Exception{
        client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("124.222.211.143"), 9300));
        // 3. 使用api创建索引
        client.admin().indices().prepareCreate("index_hello3").get();

        // 4. 关闭client
        client.close();
    }

    @Test
    public void setMappings() throws Exception{

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id")
                .field("type", "integer").field("store","yes")
                .endObject()
                .startObject("title")
                .field("type", "string").field("store","yes").field("analyzer","ik_smart")
                .endObject()
                .startObject("content")
                .field("type", "string").field("store","yes").field("analyzer","ik_smart")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        // 3. 使用api创建mapping
        client.admin().indices().preparePutMapping("index_hello2").setType("article").setSource(builder).get();

        // 4. 关闭client
        client.close();
    }

    @Test
    public void testAddDocument() throws Exception{
        //创建文档对象
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .field("id",21)
                    .field("title","今日天气预报")
                    .field("content","今日天气28摄氏度")
                .endObject();
        //把文档对象添加到索引库
        client.prepareIndex()
                //索引名
                .setIndex("index_hello")
                .setType("article")
                .setId("2")
                // 上面写的文档对象
                .setSource(builder)
                //执行操作
                .get();

        client.close();
    }

    @Test
    public void testAddDocument2() throws Exception{
        Article article = new Article();
        article.setId(31);
        article.setTitle("今日解说");
        article.setContent("今日解说电影血战钢锯岭");
        // 把article对象转成json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDocument = objectMapper.writeValueAsString(article);
        System.out.println(jsonDocument);
        // 使用client对象把文档写入索引库
        client.prepareIndex("index_hello","article","3")
                .setSource(jsonDocument, XContentType.JSON)
                .get();

        client.close();
    }

    @Test
    public void testAddDocument3() throws Exception{
        for (int i = 4; i < 100; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("今日解说" + i);
            article.setContent("今日解说电影血战钢锯岭" + i);
            // 把article对象转成json字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonDocument = objectMapper.writeValueAsString(article);
            System.out.println(jsonDocument);
            // 使用client对象把文档写入索引库
            client.prepareIndex("index_hello","article",i + "")
                    .setSource(jsonDocument, XContentType.JSON)
                    .get();
        }
        client.close();

    }
}
