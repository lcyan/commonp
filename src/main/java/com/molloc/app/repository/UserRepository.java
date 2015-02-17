package com.molloc.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.molloc.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	User findByLoginName(String loginName);
}
