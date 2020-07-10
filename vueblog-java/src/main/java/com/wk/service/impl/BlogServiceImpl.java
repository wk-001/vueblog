package com.wk.service.impl;

import com.wk.entity.Blog;
import com.wk.mapper.BlogMapper;
import com.wk.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
