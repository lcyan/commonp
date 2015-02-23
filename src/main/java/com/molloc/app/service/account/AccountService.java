package com.molloc.app.service.account;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.molloc.app.entity.User;
import com.molloc.app.repository.UserRepository;
import com.molloc.app.service.BaseService;
import com.molloc.app.service.ServiceException;
import com.molloc.app.service.account.ShiroDbRealm.ShiroUser;
import com.molloc.app.web.utils.Clock;
import com.molloc.app.web.utils.DigestUtils;
import com.molloc.app.web.utils.DynamicSpecifications;
import com.molloc.app.web.utils.EncodeUtils;
import com.molloc.app.web.utils.SearchFilter;

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
		return userRepository.findAll();
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
		if (StringUtils.isNotBlank(user.getPlainPwd()))
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

	/**
	 * 查询所有用户带分页
	 * 
	 * @param searchParams
	 * @param pageNumber 第几页
	 * @param pageSize 每页显示条数
	 * @param sortType 排序规则
	 * @return
	 */
	public Page<User> getAllUserByPage(Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<User> spec = buildSpecification(searchParams);
		return userRepository.findAll(spec, pageRequest);
	}

	private Specification<User> buildSpecification(Map<String, Object> searchParams)
	{
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
		return spec;
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType)
	{
		Sort sort = null;
		if ("auto".equals(sortType))
		{
			sort = new Sort(Direction.DESC, "id");
		} else if ("email".equals(sortType))
		{
			sort = new Sort(Direction.ASC, "email");
		}

		return new PageRequest(pageNumber - 1, pageSize, sort);
	}

}
