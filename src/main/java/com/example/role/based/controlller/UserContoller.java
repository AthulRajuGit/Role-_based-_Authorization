package com.example.role.based.controlller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.role.based.CONSTATNT.Userconstant;
import com.example.role.based.entity.User;
import com.example.role.based.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserContoller {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping("/join")
	public String joinUser(@RequestBody User user) {
	       
		String encryPass=passwordEncoder.encode(user.getPassWord());
		user.setPassWord(encryPass);
		user.setRoles(Userconstant.DEFAULT_ROLE);
		userRepository.save(user);
	         
	       
	    return "hi"+user.getUserName()+"is saved successfully!!!";
	}
	
	
	
	@GetMapping("/access/{userId}/{userRole}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String giveAccessToUser(@PathVariable int userId,@PathVariable String userRole,Principal principal) {
	
		User user=userRepository.findById(userId).get();
		String newRole="";
		List<String> activeRoles=getqRolesByLoggedInUser(principal);
		if(activeRoles.contains(userRole)) {
			newRole=user.getRoles()+","+userRole;
		}
		userRepository.save(user);
		return "Hi " + user.getUserName() + " New Role assign to you by " + principal.getName();
		
		
	}
    
	    @GetMapping
	    @Secured("ROLE_ADMIN")
	    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public List<User> loadUsers() {
	        return userRepository.findAll();
	    }

	    @GetMapping("/test")
	    @PreAuthorize("hasAuthority('ROLE_USER')")
	    public String testUserAccess() {
	        return "user can only access this !";
	    }

    
	private List<String> getqRolesByLoggedInUser(Principal principal) {
		// TODO Auto-generated method stub
		String roles=getLoggedInUser(principal).getRoles();
		List<String> assignRoles=Arrays.stream(roles.split(",")).collect(Collectors.toList());
		if(assignRoles.contains("ROLE_ADMIN"))
			return Arrays.stream(Userconstant.ADMIN_ROLE).collect(Collectors.toList());
		if(assignRoles.contains("ROLE_MODERATOR"))
		    return Arrays.stream(Userconstant.MODERATOR_ROLE).collect(Collectors.toList());
		return Collections.emptyList();
	}



	private User getLoggedInUser(Principal principal) {
		// TODO Auto-generated method stub
		
		return userRepository.findByName(principal.getName()).get();
	}


}
