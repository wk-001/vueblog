package com.wk.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wk.dto.LoginDto;
import com.wk.entity.User;
import com.wk.service.UserService;
import com.wk.utils.JwtUtils;
import com.wk.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 用户登录
	 */
	@PostMapping("login")
	public Result<Map<String,Object>> login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
		User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
		//判断user是否为空，如果为空就显示参数2的message
		Assert.notNull(user, "用户不存在");

		if (!StringUtils.equals(user.getPassword(), SecureUtil.md5(loginDto.getPassword()))) {
			return Result.fail("账号或密码错误");
		}

		//生成jwt token
		String token = jwtUtils.generateToken(user.getId());

		//将token放到响应头中
		response.setHeader("Authorization", token);
		response.setHeader("Access-control-Expose-Headers", "Authorization");

		return Result.ok(MapUtil.builder(new HashMap<String,Object>())
				.put("id", user.getId())
				.put("username", user.getUsername())
				.put("avatar", user.getAvatar())
				.put("email", user.getEmail())
				.map()
		);
	}

	/**
	 * 退出登录
	 */
	@RequiresAuthentication
	@GetMapping
	public Result<Void> logout(){
		SecurityUtils.getSubject().logout();
		return Result.ok();
	}

}
