package org.coursera.capstone.gift;


import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.coursera.capstone.gift.repository.UserEntity;
import org.coursera.capstone.gift.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class UsersController {

	@Autowired
	private UserEntityRepository users;
	private int currentId=0;
	
	@PreAuthorize("hasRole(user)")
	@RequestMapping(value="/user", method=RequestMethod.GET)
	public @ResponseBody Collection<UserEntity> getUsersList(){
		return Lists.newArrayList(users.findAll());
	}
	
	@RequestMapping(value="/user/top", method=RequestMethod.GET)
	public @ResponseBody Collection<UserEntity> getUsersTopList(){
		List<UserEntity> list= Lists.newArrayList(users.findAll());
		Comparator<UserEntity> sortCount=new Comparator<UserEntity>() {
			
			@Override
			public int compare(UserEntity o1, UserEntity o2) {
				// TODO Auto-generated method stub
				if (o1.getGiftCounts()>=o2.getGiftCounts())
				{
					return -1;
				}
				else 
					return 1;
			}
		};
		Collections.sort(list,sortCount );
		int top=10;
		if (list.size()-1>(top+1))
		{
		return list.subList(0, top+1);
		}
		else
			return list.subList(0, list.size());
	}
	@RequestMapping(value="/user", method=RequestMethod.POST)
	public @ResponseBody UserEntity addUser(@RequestBody UserEntity user){
		return save(user);
	}
	@RequestMapping(value="/user/auth", method=RequestMethod.GET)
	public @ResponseBody UserEntity getAuthorities(Principal p){
		return (UserEntity) (users.findByName(p.getName()).toArray())[0];
	
	}
	
	
	
	@RequestMapping(value="/user/gift_counts",method=RequestMethod.POST)
	public @ResponseBody UserEntity addGiftToCount(Principal p){
		if (users.findByName(p.getName())!=null&& (users.findByName(p.getName()).size()>0))
				{
		UserEntity user= (UserEntity)(users.findByName(p.getName()) .toArray())[0];
		user.setGiftCounts(user.getGiftCounts()+1);
		
		return save(user);
				}
		else
			return null;
		}
	
	
	
	public UserEntity save(UserEntity entity) {
		checkAndSetId(entity);
		
		users.save(entity);
		return entity;
	}

	private void checkAndSetId(UserEntity entity) {
		if(entity.getId().equals("0")){
			currentId+=1;
			entity.setId(currentId+"");
		}
		}
}
