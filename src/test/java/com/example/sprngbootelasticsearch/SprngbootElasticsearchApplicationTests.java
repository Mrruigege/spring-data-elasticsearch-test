package com.example.sprngbootelasticsearch;

import com.example.sprngbootelasticsearch.entity.Item;
import com.example.sprngbootelasticsearch.entity.User;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SpringBootTest
class SprngbootElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }


    @Test
    public void testCreat() {
        restTemplate.createIndex(Item.class);
        restTemplate.putMapping(Item.class);
    }

    @Test
    public void userCreate() {
        restTemplate.createIndex(User.class);
        restTemplate.putMapping(User.class);
    }
}
