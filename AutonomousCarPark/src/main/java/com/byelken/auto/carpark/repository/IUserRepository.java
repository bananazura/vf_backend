package com.byelken.auto.carpark.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.byelken.auto.carpark.model.User;

@Transactional
public interface IUserRepository
{
	Optional<User> findByEmail(String email);
}
