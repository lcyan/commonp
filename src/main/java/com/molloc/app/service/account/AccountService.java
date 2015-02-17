package com.molloc.app.service.account;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.molloc.app.entity.User;
import com.molloc.app.repository.UserRepository;
import com.molloc.app.service.BaseService;
import com.molloc.app.service.ServiceException;
import com.molloc.app.service.account.ShiroDbRealm.ShiroUser;
import com.molloc.app.web.utils.Clock;
import com.molloc.app.web.utils.DigestUtils;
import com.molloc.app.web.utils.EncodeUtils;

@Service
@Transactional
public class AccountService extends BaseService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6734275075292127948L;
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;

	private Clock clock = Clock.DEFAULT;

	private static final int SALT_SIZE = 8;

	@Autowired
	private UserRepository userRepository;

	/**
	 * 查询所有的用户
	 * 
	 * @return
	 */
	public List<User> getAllUser()
	{
		return (List<User>) userRepository.findAll();
	}

	/**
	 * 根据主键ID查询用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(Long id)
	{
		return userRepository.findOne(id);
	}

	/**
	 * 根据登录名查询用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User findUserByLoginName(String loginName)
	{
		return userRepository.findByLoginName(loginName);
	}

	/**
	 * 注册用户
	 * 
	 * @param user
	 */
	public void registerUser(User user)
	{
		encryptPassword(user);
		user.setCreatedTime(clock.getCurrentDate());

		userRepository.save(user);
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 */
	public void updateUser(User user)
	{
		if (StringUtils.isNotBlank(user.getLoginPwd()))
		{
			encryptPassword(user);
		}
		userRepository.save(user);
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 */
	public void deleteUser(Long id)
	{
		if (isSupervisor(id))
		{
			logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userRepository.delete(id);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id)
	{
		return id == 1;
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName()
	{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.loginName;
	}

	private void encryptPassword(User user)
	{
		byte[] salt = DigestUtils.generateSalt(SALT_SIZE);
		user.setSalt(EncodeUtils.encodeHex(salt));

		byte[] hashPassword = DigestUtils.sha1(user.getLoginPwd().getBytes(), salt, HASH_INTERATIONS);
		user.setLoginPwd(EncodeUtils.encodeHex(hashPassword));
	}

}
