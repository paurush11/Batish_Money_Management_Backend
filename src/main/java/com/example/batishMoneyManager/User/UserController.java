package com.example.batishMoneyManager.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
	
	private final UserService service;
	@GetMapping("/")
	public ResponseEntity<List<User>> getUsers(){
		List<User> users = service.getAllUsers().isEmpty() ? new ArrayList<User>(): service.getAllUsers();
		System.out.println(users);
		return ResponseEntity.ok(users);
	}
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Optional<User>> getUser(@PathVariable Integer id){
		System.out.println(id);
		Optional<User> savedUser =service.getUserById(id);
		return ResponseEntity.ok(savedUser);
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username){
		Optional<User> savedUser =service.getUserByUsername(username);
		return ResponseEntity.ok(savedUser);
	}
	@GetMapping("/{email:.+@.+.+}")
	public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email){
		System.out.println(email);
		Optional<User> savedUser = service.getUserByEmail(email);
		return ResponseEntity.ok(savedUser);
	}
	@PostMapping(value = "/")
	public ResponseEntity<User> SaveUser(@Valid @RequestBody User user) {
		User savedUser = service.saveUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	@PutMapping("/{id:\\d+}")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User user){
		User updatedUser = service.updateUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id:\\d+}")
	public ResponseEntity<Void> deleteUser (@PathVariable Integer id) throws Exception{
		Optional<User> user = service.getUserById(id);
		if(user.isPresent()) service.deleteUser(user.get());
		else{
			throw new Exception("User Not found");
		}
		return ResponseEntity.ok().build();
	}

}
