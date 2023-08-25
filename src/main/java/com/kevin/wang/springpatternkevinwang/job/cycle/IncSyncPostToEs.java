package com.kevin.wang.springpatternkevinwang.job.cycle;

import com.kevin.wang.springpatternkevinwang.esdto.PostEsDao;
import com.kevin.wang.springpatternkevinwang.mapper.PostMapper;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostEsDto;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步帖子到 es
 *
 * @author wangshenghui
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncPostToEs {
    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostMapper postMapper;
    // 增量数据到ES中,每分钟同步一次
    @Scheduled(fixedRate = 1000 * 60 )
    public void run() {
        Date fiveMinuteAgo = new Date(System.currentTimeMillis() - 1000 * 60*5L);
        log.info("");
        List<Post> postList = postMapper.listPostWithDelete(fiveMinuteAgo);
        List<PostEsDto> postVoList = postList.stream().map(PostEsDto::objToVo).collect(Collectors.toList());
        final Integer pageSize = 500;
        int size = postVoList.size();
        // 批量存入
        for(int i = 0;i<size;i+=pageSize){
            int end = Math.min(i+pageSize,size);
            log.info("sync from {} to {}", i, end);
            postEsDao.saveAll(postVoList.subList(i,end));
        }
        log.info("IncSyncPostToEs end, total {}", size);
    }
}
