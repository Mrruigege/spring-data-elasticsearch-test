package com.example.sprngbootelasticsearch.repository;

import com.example.sprngbootelasticsearch.entity.Item;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 可以返回的类型
 * List<T>
 * Stream<T>
 * SearchHits<T>
 * List<SearchHit<T>>
 * Stream<SearchHit<T>>
 * SearchPage<T>
 *
 * @author xiaorui
 */
@Repository
public interface ItemRepository extends ElasticsearchRepository<Item, Long> {
    List<Item> findAllById(Long id, Pageable pageable);
    Page<Item> findAllByTitle(String title, PageRequest pageable);
}
