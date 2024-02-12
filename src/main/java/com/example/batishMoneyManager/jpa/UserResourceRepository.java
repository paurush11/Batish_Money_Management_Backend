package com.example.batishMoneyManager.jpa;

import com.example.batishMoneyManager.User.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserResourceRepository extends JpaRepository<User, Integer>{
	<S extends User> S save(S entity);

	<S extends User> List<S> saveAll(Iterable<S> entities);

	Optional<User> findById(@Param("id") Integer Id);

	List<User> findAll();

	void delete(User entity);

	void deleteAll();
	
	List<User> findByFirstName(String firstname);
	Optional<User> findByUserName(String username);

	List<User> findByFirstNameAndUserName(String firstname, String username);
	
	Optional<User> findByEmail(String email);
	
}
