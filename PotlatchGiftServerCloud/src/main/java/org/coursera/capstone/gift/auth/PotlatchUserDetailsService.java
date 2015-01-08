package org.coursera.capstone.gift.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.coursera.capstone.gift.repository.UserEntity;
import org.coursera.capstone.gift.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PotlatchUserDetailsService implements UserDetailsService {

	@Autowired
	private UserEntityRepository repo;
	
	public PotlatchUserDetailsService(){
		super();
	}
	public PotlatchUserDetailsService(UserEntityRepository repo) {
		super();
	this.repo = repo;
	System.out.print(repo==null?"null":"not null");
	try{
	createUsers();
	}
	catch(org.springframework.dao.EmptyResultDataAccessException e){}
	}
	
	//Predefined users 
	 public void createUsers() throws org.springframework.dao.EmptyResultDataAccessException {
		// TODO Auto-generated constructor stub
		 Collection<UserDetails> users=Arrays.asList(
					User.create("admin", "pass", "ADMIN", "USER"),
					User.create("user0", "pass", "USER"),
					User.create("user1", "pass", "USER"),
					User.create("user2", "pass", "USER"),
					User.create("tempuser", "temp", "USER")
					);
		 for(UserDetails user: users)
		 {
			 
			 create(user);
		 }
		 try
		 {
		 System.out.println(repo.count()+"");
		 }
		 catch(Exception e){}
	}
	 public void create(UserDetails user) throws org.springframework.dao.EmptyResultDataAccessException
	 {
		if ((repo.findByName(user.getUsername())==null) || (repo.findByName(user.getUsername()).size()==0))
		 repo.save(new UserEntity(user.getUsername(),user.getPassword()));
		 try{
		
		 System.out.println(user.getUsername());
		 System.out.println((UserEntity) (repo.findByName(user.getUsername()).toArray())[0]);
		 }
		 catch(Exception e){}
	 }
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Collection<UserEntity> users=repo.findByName(username);
		if(users==null || users.size()==0)
		{
	
		return null;
		}
		else
		{
			UserEntity user=(UserEntity)(users.toArray())[0];
		List<GrantedAuthority> auth = AuthorityUtils
		.commaSeparatedStringToAuthorityList("ROLE_USER");
		if (username.equals("admin")) {
		auth = AuthorityUtils
		.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
		}
		String password = user.getPassword();
		return new org.springframework.security.core.userdetails.User(username, password,
		auth);
		}
	}
	

}
