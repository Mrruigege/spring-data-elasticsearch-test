package com.example.sprngbootelasticsearch.estest;

import com.example.sprngbootelasticsearch.entity.Item;
import com.example.sprngbootelasticsearch.repository.ItemRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * es增删改查
 * @author xiaorui
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EsTest{

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 单个对象sava
     */
    @Test
    public void testSave() {
        Item item = new Item(1L, "小米手机", "手机", "小米", 3490.00, "http://image.zq.com/13123.jpg");
        itemRepository.save(item);
    }

    /**
     * 传入list
     * 重复sava就是update
     */
    @Test
    public void testSaveAll() {
//        List<Item> list = new ArrayList<>();
//        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.zq.com/123.jpg"));
//        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.zq.com/3.jpg"));
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.zq.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.zq.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.zq.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.zq.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.zq.com/13123.jpg"));
        itemRepository.saveAll(list);
    }

    @Test
    public void testFindAll() {
        Iterable<Item> items = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        items.forEach(item -> System.out.println(item));
    }

    @Test
    public void testFindAllPage() {
        //itemRepository.findAll(PageRequest.of(0, 2));
//        List<Item> items = itemRepository.findAllById(1L, PageRequest.of(0, 2));
//        items.forEach(item -> System.out.println(item));
        Page<Item> items = itemRepository.findAllByTitle("小米", PageRequest.of(0, 2));
        System.out.println(items.getTotalPages());
        //items.forEach(item -> System.out.println(item));
    }

    /**
     * 匹配
     */
    @Test
    public void testQuery() {
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", "小米");
        Iterable<Item> items = itemRepository.search(query);
        items.forEach(item -> System.out.println(item));
    }

    /**
     * 分页查询
     */
    @Test
    public void testPageQuery() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("category", "手机"));
        // 构建分页查询
        int page = 1;
        int size = 3;
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 执行搜索，获取结果
        Page<Item> items = itemRepository.search(queryBuilder.build());
        System.out.println("总页数：" + items.getTotalPages());
        System.out.println("总条数: " + items.getTotalElements());
        System.out.println("每页大小：" + items.getSize());
        System.out.println("当前页：" + items.getNumber());
        items.forEach(item -> System.out.println(item));
    }

    /**
     * 排序查询
     */
    @Test
    public void testSortQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));
        // 设置排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        // 执行查询
        Page<Item> items = itemRepository.search(queryBuilder.build());
        items.forEach(item -> System.out.println(item));
    }

    /**
     * 测试聚合查询，聚合查询其实就是将查询结果按某个结果字段分组
     */
    @Test
    public void testAgg() {
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//
//        // 不查询任何结果
//        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
//        // 添加一个聚合，聚合类型为brands, 聚合字段为brand
//        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));
//        // 执行查询
//        AggregatedPage<Item> result = restTemplate.queryForPage(queryBuilder.build(), Item.class);
//        // 解析聚合
//        Aggregations agg = result.getAggregations();
//        StringTerms terms = agg.get("brands");
//        // 获取桶
//        List<StringTerms.Bucket> buckets = terms.getBuckets();
//        // 遍历
//        for (StringTerms.Bucket bucket: buckets) {
//            System.out.println(bucket.getKeyAsString());
//            System.out.println(bucket.getDocCount());
//        }
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        // 3、解析
        // 3.1、从结果中取出名为brands的那个聚合，
        // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
        }
    }
}
