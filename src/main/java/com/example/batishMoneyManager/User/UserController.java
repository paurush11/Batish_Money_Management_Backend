package com.example.batishMoneyManager.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
		return ResponseEntity.ok(users);
	}
	@GetMapping("/{id:\\d+}")
	public ResponseEntity<Optional<User>> getUser(@PathVariable Integer id){
		System.out.println(id);
		Optional<User> savedUser =service.getUserById(id);
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
}
