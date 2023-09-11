package com.kevin.wang.springpatternkevinwang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.model.entity.User;
import com.google.gson.Gson;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.constant.CommonConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.mapper.PostMapper;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostEsDto;
import com.kevin.wang.springpatternkevinwang.model.dto.post.PostQueryRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import com.kevin.wang.springpatternkevinwang.model.entity.PostFavour;
import com.kevin.wang.springpatternkevinwang.model.entity.PostThumb;
import com.kevin.wang.springpatternkevinwang.model.vo.PostVO;
import com.kevin.wang.springpatternkevinwang.model.vo.UserVO;
import com.kevin.wang.springpatternkevinwang.service.PostFavourService;
import com.kevin.wang.springpatternkevinwang.service.PostService;
import com.kevin.wang.springpatternkevinwang.service.PostThumbService;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wang
 * @create 2023-2023-21-23:47
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private static final Gson gson = new Gson();
    @Resource
    private PostThumbService postThumbService;

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private UserService userService;

    @Resource
    private ElasticsearchTemplate elasticsearchRestTemplate;


    @Override
    public void validPost(Post post, boolean add) {
        ThrowUtils.throwIf(post==null, ErrorCode.PARAMS_ERROR);

        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        // 如果任何这三个字段为空，那么就抛出异常
        ThrowUtils.throwIf(StringUtils.isAllBlank(title,content,tags),ErrorCode.PARAMS_ERROR);

        if(title.length()>100){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"标题过长");
        }

        if(content!=null && content.length()>8152){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"内容过长");
        }
    }

    @Override
    public QueryWrapper<Post> getQueryWrapper(PostQueryRequest queryRequest) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if(queryRequest==null){
            return queryWrapper;
        }

        String title = queryRequest.getTitle();
        String content = queryRequest.getContent();
        Long id = queryRequest.getId();
        List<String> tags = queryRequest.getTags();
        String searchText = queryRequest.getSearchText();
        Long userId = queryRequest.getUserId();
        Long notId = queryRequest.getNotId();
        if(StringUtils.isNotBlank(searchText)){
            queryWrapper.lambda().like(Post::getTitle,searchText)
                    .or().like(Post::getContent,searchText);
        }

        queryWrapper.lambda().like(StringUtils.isNotBlank(title),Post::getTitle,title)
                .like(StringUtils.isNotBlank(content),Post::getContent,content);
        if(tags!=null) {
            for (String tag : tags) {
                queryWrapper.lambda().like(StringUtils.isNotBlank(tag), Post::getTags, "\"" + tag + "\"");
            }
        }
        queryWrapper.lambda().ne(notId!=null,Post::getId,notId)
                .eq(id!=null,Post::getId,id)
                .eq(userId!=null,Post::getUserId,userId)
                .eq(Post::getIsDelete,0);
        return queryWrapper;
    }


    @Override
    public Page<Post> searchFromEs(PostQueryRequest postQueryRequest) {
        Long id = postQueryRequest.getId();
        Long notId = postQueryRequest.getNotId();
        String searchText = postQueryRequest.getSearchText();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTags();
        List<String> orTagList = postQueryRequest.getOrTags();
        Long userId = postQueryRequest.getUserId();
        // es 起始页为 0
        long current = postQueryRequest.getCurrent() - 1;
        long pageSize = postQueryRequest.getPageSize();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (notId != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任何一个标签即可
        if (CollectionUtils.isNotEmpty(orTagList)) {
            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String tag : orTagList) {
                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
            }
            orTagBoolQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.filter(orTagBoolQueryBuilder);
        }
        // 按关键词检索
        if (StringUtils.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(content)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
        SearchHits<PostEsDto> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDto.class);
        Page<Post> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Post> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<PostEsDto>> searchHitList = searchHits.getSearchHits();
            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            List<Post> postList = baseMapper.selectBatchIds(postIdList);
            if (postList != null) {
                Map<Long, List<Post>> idPostMap = postList.stream().collect(Collectors.groupingBy(Post::getId));
                postIdList.forEach(postId -> {
                    if (idPostMap.containsKey(postId)) {
                        resourceList.add(idPostMap.get(postId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDto.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        return page;
    }


    @Override
    public PostVO getPostVO(Post post, HttpServletRequest request) {
        if(post==null){
            return null;
        }
        PostVO postVo = PostVO.objToVo(post);
        Long postId = post.getId();
        Long userId = post.getUserId();
        User user = null;
        if(userId!=null && userId>0){
            user = userService.getById(userId);
        }

        UserVO userVo = userService.getUserVO(user);

        postVo.setUser(userVo);
        User loginUser = userService.getLoginUser(request);
        if(postId!=null && postId>0){
            QueryWrapper<PostThumb> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(PostThumb::getPostId, postId);
            wrapper.lambda().eq(PostThumb::getUserId, loginUser.getId());
            PostThumb postThumb = postThumbService.getOne(wrapper);
            postVo.setHasFavour(postThumb!=null);

            wrapper = null;

            QueryWrapper<PostFavour> favourWrapper = new QueryWrapper<>();
            favourWrapper.lambda().eq(PostFavour::getPostId, postId);
            favourWrapper.lambda().eq(PostFavour::getUserId, loginUser.getId());
            PostFavour postFavour = postFavourService.getOne(favourWrapper);
            postVo.setHasFavour(postFavour!=null);
        }
        return postVo;
    }

    @Override
    public Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request) {
        Page<PostVO> postVoPage = new Page<>();
        postVoPage.setTotal(postPage.getTotal()).setSize(postPage.getSize()).setCurrent(postPage.getCurrent());

        List<Post> postList = postPage.getRecords();

        Set<Long> userIdSet = postList.stream().map(post -> post.getUserId()).collect(Collectors.toSet());

        List<User> userList = userService.listByIds(userIdSet);

        Map<Long, List<User>> userMap = userList.stream().collect(Collectors.groupingBy(User::getId));

        User loginUser = userService.getLoginUserPermitNull(request);

        Map<Long,Boolean> thumbMap = new HashMap<>();
        Map<Long,Boolean> favourMap = new HashMap<>();

        if(loginUser!=null){
            Set<Long> postIdSet = postList.stream().map(Post::getId).collect(Collectors.toSet());
            QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>();
            thumbQueryWrapper.lambda().eq(PostThumb::getUserId, loginUser.getId());
            thumbQueryWrapper.lambda().in(PostThumb::getPostId, postIdSet);
            List<PostThumb> postThumbList = postThumbService.list(thumbQueryWrapper);

            postThumbList.stream().forEach(postThumb -> {
                thumbMap.put(postThumb.getPostId(),true);
            });
            QueryWrapper<PostFavour> favourQueryWrapper = new QueryWrapper<>();
            favourQueryWrapper.lambda().eq(PostFavour::getUserId, loginUser.getId());
            favourQueryWrapper.lambda().in(PostFavour::getPostId, postIdSet);
            List<PostFavour> postFavourList = postFavourService.list(favourQueryWrapper);
            postFavourList.stream().forEach(postFavour -> {
                favourMap.put(postFavour.getPostId(),true);
            });
        }
        List<PostVO> postVoList = postList.stream().map(post -> {
            PostVO postVo = PostVO.objToVo(post);
            postVo.setUser(userService.getUserVO(userMap.get(post.getUserId()).get(0)));
            Long postId = post.getId();
            postVo.setHasThumb(thumbMap.containsKey(postId));
            postVo.setHasFavour(favourMap.containsKey(postId));
            return postVo;
        }).collect(Collectors.toList());
        postVoPage.setRecords(postVoList);
        return postVoPage;
    }
}
