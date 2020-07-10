package com.wk.shiro;

import com.wk.entity.User;
import com.wk.service.UserService;
import com.wk.utils.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户登录、授权组件
 */
@Component
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	//设置支持jwt token
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	/**
	 * 登录认证，验证用户身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		JwtToken jwtToken = (JwtToken) token;

		String userId = jwtUtils.getClaimByToken(jwtToken.getPrincipal().toString()).getSubject();

		User user = userService.getById(userId);

		if (user == null) {
			throw new UnknownAccountException("账户不存在");
		}

		if (user.getStatus() == -1) {
			throw new LockedAccountException("账户被锁定");
		}

		return new SimpleAuthenticationInfo(user,jwtToken.getCredentials(),getName());
	}

	/**
	 * 用户验证通过 获取用户的角色权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}
