package com.wk.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wk.utils.JwtUtils;
import com.wk.utils.Result;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		//从请求头中获取JWT Token
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String jwt = request.getHeader("Authorization");
		if (StringUtils.isNotEmpty(jwt)) {
			return new JwtToken(jwt);
		}
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String jwt = request.getHeader("Authorization");
		if (StringUtils.isNotEmpty(jwt)) {
			//校验jwt 并登录
			Claims claims = jwtUtils.getClaimByToken(jwt);
			if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
				//抛出token失效异常
				throw new ExpiredCredentialsException("token已失效");
			}
			//登录
			return executeLogin(servletRequest, servletResponse);
		}else{
			return true;	//放行
		}
	}

	//重写登录失败的方法，返回登录失败的原因
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		Throwable throwable = e.getCause() == null ? e : e.getCause();

		Result<String> result = Result.fail(throwable.getMessage());

		String json = JSONUtil.toJsonStr(result);

		try {
			httpServletResponse.getWriter().println(json);
		} catch (IOException ioException) {

		}

		return false;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
			return false;
		}

		return super.preHandle(request, response);
	}
}
