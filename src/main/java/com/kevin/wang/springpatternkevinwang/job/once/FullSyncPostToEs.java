package com.kevin.wang.springpatternkevinwang.job.once;

import com.kevin.wang.springpatternkevinwang.esdto.PostEsDao;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostEsDto;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wang
 * @create 2023-2023-21-18:19
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Resource
    private PostEsDao postEsDao;


    @Override
    public void run(String... args) throws Exception {
        List<Post> postList = postService.list();

        if(postList==null || postList.size()==0){
            return;
        }

        List<PostEsDto> postEsDtoList = postList.stream().map(PostEsDto::objToVo).collect(Collectors.toList());

        final int pageSize = 500;
        int size = postEsDtoList.size();

        for(int i = 0;i<size;i+=pageSize){
            int end = Math.min(i+pageSize,size);
            log.info("IncSyncPostToEs from {} to {}",i, size);
            postEsDao.saveAll(postEsDtoList.subList(i,end));

        }
        log.info("FullSyncPostToEs end, total {}", size);
    }
}
