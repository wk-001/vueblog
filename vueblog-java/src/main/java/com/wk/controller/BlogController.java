package com.wk.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.entity.Blog;
import com.wk.service.BlogService;
import com.wk.utils.Result;
import com.wk.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService  blogService;

    @GetMapping("list")
    public Result<IPage<Blog>> list(@RequestParam(defaultValue = "1") Integer currentPage){
        Page<Blog> page = new Page<>(currentPage,5);
        return Result.ok(blogService.page(page,new QueryWrapper<Blog>().orderByDesc("created")));
    }

    @GetMapping("{id}")
    public Result<Blog> detail(@PathVariable("id") Long id){
        return Result.ok(blogService.getById(id));
    }

    @RequiresAuthentication
    @PostMapping
    public Result<Void> edit(@Validated @RequestBody Blog blog){

        Blog temp = null;

        if (blog.getId() != null) {
            temp = blogService.getById(blog.getId());

            //只能编辑自己的文章
            Assert.isTrue(temp.getUserId().equals(ShiroUtil.getProfile().getId()),"无权限");
        }else {
            temp = new Blog();

            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        BeanUtils.copyProperties(blog, temp,"id","userId","created","status");

        blogService.saveOrUpdate(temp);

        return Result.ok();
    }

}

