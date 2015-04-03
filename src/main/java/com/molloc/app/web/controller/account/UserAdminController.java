package com.molloc.app.web.controller.account;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.molloc.app.entity.User;
import com.molloc.app.service.account.AccountService;
import com.molloc.app.web.controller.BaseController;
import com.molloc.app.web.utils.Servlets;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController extends BaseController
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904114252991517307L;

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static
	{
		sortTypes.put("auto", "自动");
		sortTypes.put("email", "邮箱");
	}

	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
			ServletRequest request)
	{
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<User> users = accountService.getAllUserByPage(searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("users", users);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "account/adminUserList";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model)
	{
		model.addAttribute("user", accountService.getUser(id));
		return "account/adminUserForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes)
	{
		accountService.updateUser(user);
		redirectAttributes.addFlashAttribute("message", "更新用户" + user.getLoginName() + "成功");
		return "redirect:/admin/users";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes)
	{
		User user = accountService.getUser(id);
		accountService.deleteUser(id);
		redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
		return "redirect:/admin/users";
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model)
	{
		if (id != -1)
		{
			model.addAttribute("user", accountService.getUser(id));
		}
	}
}
