package com.kevin.wang.springpatternkevinwang.esdto;

import com.kevin.wang.springpatternkevinwang.model.dto.post.PostEsDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-15:24
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDto, Long> {

    List<PostEsDao> findByUserId(Long userId);
}
