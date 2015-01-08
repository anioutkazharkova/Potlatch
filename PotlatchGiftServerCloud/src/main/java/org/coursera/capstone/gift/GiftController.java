package org.coursera.capstone.gift;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.coursera.capstone.gift.client.GiftEntitySvcApi;
import org.coursera.capstone.gift.repository.CommentEntity;
import org.coursera.capstone.gift.repository.CommentEntityRepository;
import org.coursera.capstone.gift.repository.GiftChainEntity;
import org.coursera.capstone.gift.repository.GiftChainRepository;
import org.coursera.capstone.gift.repository.GiftEntity;
import org.coursera.capstone.gift.repository.GiftEntityRepository;
import org.coursera.capstone.gift.repository.ImageEntityRepository;
import org.coursera.capstone.gift.repository.UserEntity;
import org.coursera.capstone.gift.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class GiftController {

	@Autowired
	private GiftEntityRepository gifts;
	@Autowired
	private UserEntityRepository users;
	@Autowired
	private ImageEntityRepository images;
	
	@Autowired
	private CommentEntityRepository comments;
	
	@Autowired
	private GiftChainRepository chains;
	
	private int currentId=0;
	private int currentCommentId=0;
	private int currentChainId=0;
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_SVC_PATH,method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> getGiftsList(){
		List<GiftEntity> giftCollection= Lists.newArrayList(gifts.findAll());
		
		 Collections.reverse(giftCollection);
		 return giftCollection;
		//return Lists.newArrayList(gifts.findAll(new PageRequest(0, 10,Sort.Direction.DESC,"id")));
	}
	@RequestMapping(value=GiftEntitySvcApi.GIFT_SVC_PATH+"/page/{page}",method=RequestMethod.GET)	
	public @ResponseBody Collection<GiftEntity> getGiftsPaged(@PathVariable("page") long page){
		Collection<GiftEntity> giftCollection=getGiftsList();
		ArrayList<GiftEntity> list=new ArrayList(giftCollection);
		int top=10;
		if (list.size()-1>((int)page*top)+top+1)
		{
		return list.subList((int)page*top, ((int)page*top)+top+1);
		}
		else
			return list.subList((int)page*top, list.size());
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_SVC_PATH + "/{id}",method=RequestMethod.GET)
	public @ResponseBody GiftEntity getGiftById(@PathVariable("id") String id){
		return gifts.findOne(id);
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_SVC_PATH,method=RequestMethod.POST)
	public @ResponseBody GiftEntity addGift(@RequestBody GiftEntity gift,Principal p){
		
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];
		int count=currentUser.getGiftCounts()+1;
		currentUser.setGiftCounts(count);
		users.save(currentUser);
		gift.setUserToken(currentUser.getUuid());
		gift.setUserName(p.getName());
		GiftChainEntity chain=new GiftChainEntity();
		chain.setGiftCount(1);
		GiftChainEntity received=chains.save(chain);
		gift.setChainId(received.getId());
		gift.setIsParent(1);
		return save(gift);
	}
	@RequestMapping(value=GiftEntitySvcApi.CHAIN_SVC_PATH,method=RequestMethod.POST)
	public @ResponseBody GiftEntity addGiftToChain(@RequestBody GiftEntity gift,@PathVariable("id") String id,Principal p){
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];
		int countGift=currentUser.getGiftCounts()+1;
		currentUser.setGiftCounts(countGift);
		users.save(currentUser);
		gift.setUserToken(currentUser.getUuid());
		gift.setUserName(p.getName());
		GiftChainEntity received=chains.findOne(id);
		if (received!=null)
		{
		gift.setIsParent(0);
		gift.setChainId(received.getId());
		int count=received.getGiftCount();
		received.setGiftCount(++count);
		saveChain(received);
		return save(gift);
		}
		else return null;
	}
	@RequestMapping(value=GiftEntitySvcApi.GIFT_TITLE_SEARCH_PATH,method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByTitle(@RequestParam(GiftEntitySvcApi.TITLE_PARAMETER) String title){
		return gifts.findByTitleContaining(title);
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_TITLE_SEARCH_PATH+"/page/{page}",method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByTitlePaged(@RequestParam(GiftEntitySvcApi.TITLE_PARAMETER) String title,@PathVariable("page")long page){
		List<GiftEntity> list= Lists.newArrayList(findByTitle(title));
		Collections.reverse(list);
		
		int top=10;
		if (list.size()-1>((int)page*top)+top+1)
		{
		return list.subList((int)page*top, ((int)page*top)+top+1);
		}
		else
			return list.subList((int)page*top, list.size());
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_CHAINID_SEARCH_PATH,method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByChainId(@RequestParam(GiftEntitySvcApi.CHAIN_ID) String chainId){
	return gifts.findByChainId(chainId);
	
	}
	@RequestMapping(value=GiftEntitySvcApi.GIFT_CHAINID_SEARCH_PATH+"/page/{page}",method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByChainIdPaged(@RequestParam(GiftEntitySvcApi.CHAIN_ID) String chainId,@PathVariable("page")long page){
	List<GiftEntity> list= Lists.newArrayList(findByChainId(chainId));
	//Collections.reverse(list);
	
	int top=10;
	if (list.size()-1>((int)page*top)+top+1)
	{
	return list.subList((int)page*top, ((int)page*top)+top+1);
	}
	else
		return list.subList((int)page*top, list.size());
	
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_USER_TOKEN_SEARCH_PATH,method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByUserToken(Principal p){
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];;
		return gifts.findByUserToken(currentUser.getUuid());
		
	}
	
	@RequestMapping(value=GiftEntitySvcApi.GIFT_USER_TOKEN_SEARCH_PATH+"/page/{page}",method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> findByUserTokenPaged(Principal p,@PathVariable("page")long page){
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];
		List<GiftEntity> list=new ArrayList(findByUserToken(p));
		int top=10;
		if (list.size()-1>((int)page*top)+top+1)
		{
		return list.subList((int)page*top, ((int)page*top)+top+1);
		}
		else
			return list.subList((int)page*top, list.size());
		
	}
	@RequestMapping(value=GiftEntitySvcApi.GIFT_COMMENT_PATH,method=RequestMethod.POST)
	public @ResponseBody GiftEntity addComment(@RequestBody CommentEntity comment,@PathVariable("id") String id,Principal p){
		GiftEntity currentGift=gifts.findOne(id);
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];;
		Collection<CommentEntity> commentsColl=comments.findByGiftIdAndUserToken(id, currentUser.getUuid());
		if (commentsColl==null || commentsColl.size()==0){
		comment.setGiftId(currentGift.getId());
		comment.setUserToken(currentUser.getUuid());
		if (comment.getIsTouched()>0)
		{
			int count=currentGift.getTouchCount()+1;
			currentGift.setTouchCount(count);
			save(currentGift);
		}
		saveComment(comment);
		}
		else
		{
			
			CommentEntity currentComment=(commentsColl!=null && commentsColl.size()>0)?(CommentEntity)(commentsColl.toArray())[0]:null;
			if (currentComment.getIsTouched()==0)
			{
				currentComment.setIsTouched(comment.getIsTouched());
				if (comment.getIsTouched()>0)
				{
					int count=currentGift.getTouchCount()+1;
					currentGift.setTouchCount(count);
					save(currentGift);
				}
			}
				currentComment.setIsInappropriate(comment.getIsInappropriate());
				currentComment.setIsObscene(comment.getIsObscene());
				saveComment(currentComment);
			
		}
		
		return currentGift;
	}
	
@RequestMapping(value=GiftEntitySvcApi.GIFT_FILTERED_PATH,method=RequestMethod.GET)
	public @ResponseBody Collection<GiftEntity> getGiftsFiltered(Principal p){
		UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];;
		ArrayList<GiftEntity> list=new ArrayList<GiftEntity>();
		Iterable<GiftEntity> giftList=gifts.findAll();
		if (giftList!=null)
		{
		for(GiftEntity g: giftList)
		{
			Collection<CommentEntity> commentsColl=comments.findByGiftIdAndUserToken(g.getId(), currentUser.getUuid());
			CommentEntity comment=(commentsColl!=null && commentsColl.size()>0)?(CommentEntity)(commentsColl.toArray())[0]:null;
			if (comment!=null)
			{
				if (comment.getIsInappropriate()==0&&comment.getIsObscene()==0)
				{
					list.add(g);
				}
					
			}
			else
				list.add(g);
		}
		}
		return list;
	}

@RequestMapping(value=GiftEntitySvcApi.GIFT_FILTERED_PATH+"/page/{page}",method=RequestMethod.GET)
public @ResponseBody Collection<GiftEntity> getGiftsFilteredPaged(Principal p,@PathVariable("page") long page){
	UserEntity currentUser=(UserEntity) (users.findByName(p.getName()).toArray())[0];
	ArrayList<GiftEntity> list=new ArrayList<GiftEntity>();
	Iterable<GiftEntity> giftList=gifts.findAll();
	for(GiftEntity g: giftList)
	{
		CommentEntity comment=comments.findByGiftIdAndUserToken(g.getId(), currentUser.getUuid())==null?null:(CommentEntity)(comments.findByGiftIdAndUserToken(g.getId(), currentUser.getUuid()).toArray())[0];
		if (comment!=null)
		{
			if (comment.getIsInappropriate()==0&&comment.getIsObscene()==0)
			{
				list.add(g);
			}
				
		}
		else
			list.add(g);
	}
	int top=10;
	if (list.size()-1>((int)page*top)+top+1)
	{
	return list.subList((int)page*top, ((int)page*top)+top+1);
	}
	else
		return list.subList((int)page*top, list.size());
}
@RequestMapping(value=GiftEntitySvcApi.GIFT_SVC_PATH+"/{id}",method=RequestMethod.DELETE)
public @ResponseBody GiftEntity removeGift(@PathVariable("id") String id,Principal p){
	
	GiftEntity gift=gifts.findOne(id);
	if (gift!=null)
	{
	String chainId=gift.getChainId();
	UserEntity user=(UserEntity)users.findByName(p.getName()).toArray()[0];
	user.setGiftCounts(user.getGiftCounts()-1);
	users.save(user);
	if (gift.getIsParent()==1)
	{
		
		//remove all in chain
		gifts.delete(gifts.findByChainId(chainId));
		//remove chain
		chains.delete(chainId);
	}
	else
	{
     GiftChainEntity chain= chains.findOne(chainId);
     chain.setGiftCount(chain.getGiftCount()-1);
     chains.save(chain);
	gifts.delete(id);
	
	}
	}
	return new GiftEntity();
}

	public GiftChainEntity saveChain(GiftChainEntity entity) {
		checkAndSetIdChain(entity);
		
		chains.save(entity);
		return entity;
	}

	private void checkAndSetIdChain(GiftChainEntity entity) {
		if(entity.getId().equals("0")){
			currentChainId+=1;
			entity.setId(currentChainId+"");
		}
		}
	public CommentEntity saveComment(CommentEntity entity) {
		
		comments.save(entity);
		return entity;
	}

	
	
	public GiftEntity save(GiftEntity entity) {		
		
		gifts.save(entity);
		return entity;
	}

	
}
