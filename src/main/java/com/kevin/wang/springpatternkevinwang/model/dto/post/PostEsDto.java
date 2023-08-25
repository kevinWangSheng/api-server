package com.kevin.wang.springpatternkevinwang.model.dto.post;

import cn.hutool.core.bean.BeanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevin.wang.springpatternkevinwang.model.entity.Post;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-10:10
 */
// todo 取消注释开启 ES（须先配置 ES）
//@Document(indexName = "post")
@Data
public class PostEsDto implements Serializable {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private Long id;

    private String title;

    private String content;


    private List<String> tags;


    private Long userId;

    private Integer thumbNum;

    private Integer favourNum;

    @Field(index = false,store = true, format = {},type = FieldType.Date,pattern = DATE_FORMAT)
    private String createdTime;

    @Field(index = false,store = true,format = {},type = FieldType.Date,pattern = DATE_FORMAT)
    private String updatedTime;

    private Integer isDelete;

    private static final Gson GSON = new Gson();

    private static final long serialVersionUID = 1L;

    public static PostEsDto objToVo(Post post) {
        if(post==null){
            return null;
        }
        PostEsDto postEsDto  = new PostEsDto();
        BeanUtil.copyProperties(post,postEsDto);
        String tags = post.getTags();
        if(StringUtils.isNotBlank(tags)) {
            postEsDto.setTags(GSON.fromJson(tags, new TypeToken<List<String>>() {
            }.getType()));
        }

        return postEsDto;
    }


    public static Post voToObj(PostEsDto postEsDto) {
        if(postEsDto==null){
            return null;
        }
        Post post  = new Post();
        BeanUtil.copyProperties(postEsDto,post);
        List<String> tagList = postEsDto.getTags();
        if(tagList!=null){
            post.setTags(GSON.toJson(tagList));
        }
        return post;
    }
}
