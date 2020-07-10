package com.wk.config;

import com.wk.shiro.JwtFilter;
import com.wk.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*shiro配置类*/
@Configuration
public class ShiroConfig {

	@Autowired
	private JwtFilter jwtFilter;

	/**
	 * Redis管理session
	 */
	@Bean
	public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSessionDAO);
		return sessionManager;
	}


	/**
	 * 配置核心 安全事务管理器
	 */
	@Bean
	public SessionsSecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager,
												   RedisCacheManager redisCacheManager){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}


	/*@Bean
	public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager,
										   RedisCacheManager redisCacheManager){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}*/

	/**
	 * shiro过滤器链 定义那些连接需要经过那些过滤器
	 * @return
	 */
	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

		Map<String, String> filterMap = new LinkedHashMap<>();

		filterMap.put("/**", "jwt");		//主要通过注解方式校验权限
		chainDefinition.addPathDefinitions(filterMap);
		return chainDefinition;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
														 ShiroFilterChainDefinition shiroFilterChainDefinition) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);

		Map<String, Filter> filters = new HashMap<>();
		filters.put("jwt", jwtFilter);
		shiroFilter.setFilters(filters);

		Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}

}
