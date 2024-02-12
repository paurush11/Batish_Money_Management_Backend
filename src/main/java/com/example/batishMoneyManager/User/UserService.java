package com.example.batishMoneyManager.User;

import java.util.List;
import java.util.Optional;

import com.example.batishMoneyManager.jpa.UserResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

	private final UserResourceRepository repository;
	public User saveUser(User user) {
		return this.repository.save(user);
	}
	public User updateUser(User user) {
		Optional<User> fetchUser = repository.findById(user.getId());
		if(fetchUser.isPresent()) {
			this.repository.deleteById(user.getId());
		}
		return this.repository.save(user);
	}
	
	public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
	public Optional<User> getUserByUsername(String userName) {
        return repository.findByUserName(userName);
    }

	public Optional<User> getUserById(Integer Id){
		return repository.findById(Id);
	}
	public List<User> getAllUsers(){
		return repository.findAll();
	}

	public void deleteUser(User user) {
		repository.delete(user);
	}

}
