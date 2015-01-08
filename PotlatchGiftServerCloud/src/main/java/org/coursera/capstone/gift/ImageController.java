package org.coursera.capstone.gift;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coursera.capstone.gift.client.ImageEntitySvcApi;
import org.coursera.capstone.gift.repository.GiftEntity;
import org.coursera.capstone.gift.repository.GiftEntityRepository;
import org.coursera.capstone.gift.repository.ImageEntity;
import org.coursera.capstone.gift.repository.ImageEntityRepository;
import org.coursera.capstone.gift.repository.ImageStatus;
import org.coursera.capstone.gift.repository.ImageStatus.ImageState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

	@Autowired
	private ImageEntityRepository images;
	@Autowired
	private GiftEntityRepository gifts;
	
	private int currentId=0;
	private ImageFileManager manager;

	@RequestMapping(value=ImageEntitySvcApi.IMAGE_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody ImageEntity addImage(@RequestBody ImageEntity image){
		if (manager==null)
			try {
				manager=ImageFileManager.get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return save(image);
	}
	@RequestMapping(value=ImageEntitySvcApi.IMAGE_GIFT_ID_SEARCH_PATH,method=RequestMethod.GET)
	public @ResponseBody ImageEntity findByGiftId(@RequestParam("giftId") String giftId){
		
		return images.findByGiftId(giftId);
	}
	

	public ImageEntity save(ImageEntity entity) {
		//checkAndSetId(entity);
		GiftEntity currentGift= gifts.findOne(entity.getGiftId());
		ImageEntity image=images.save(entity);
		image.setUrl(getDataUrl(image.getId()));
		currentGift.setImageUrl(image.getUrl());
		gifts.save(currentGift);
		images.save(image);
		return entity;
	}

	private void checkAndSetId(ImageEntity entity) {
		if(entity.getId().equals("")){
			currentId+=1;
			entity.setId(currentId+"");
		}
		}
	public void saveSomeImage(ImageEntity v, MultipartFile data) throws IOException {
	     manager.saveData(v, data.getInputStream());
	}
	 private String getDataUrl(String imageId){
         String url = getUrlBaseForLocalServer() + "/image/" + imageId + "/data";
         return url;
     }

  	private String getUrlBaseForLocalServer() {
		   HttpServletRequest request = 
		       ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		   String base = 
		      "https://"+request.getServerName() 
		      + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
		   return base;
		}
  	
  	@RequestMapping(value=ImageEntitySvcApi.IMAGE_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody ImageStatus setImageData(@PathVariable("id")String id, @RequestParam("data") MultipartFile data,HttpServletResponse response) throws IOException  {
		// TODO Auto-generated method stub
		if (images.findOne(id)!=null)
		{
			ImageEntity image=images.findOne(id);
			
			saveSomeImage(images.findOne(id), data);
			return new ImageStatus(ImageState.READY);
		}	
		else response.sendError(404);
		return null;
		
	}
  	
  	@RequestMapping(value=ImageEntitySvcApi.IMAGE_DATA_PATH, method=RequestMethod.GET)
	public @ResponseBody void getData(@PathVariable("id")String id, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
  		try{
		if (images.findOne(id)!=null)
		{
			manager.copyData(images.findOne(id), response.getOutputStream());
		}
		else response.sendError(404);
  		}
  		catch(Exception e){}
		
	}
}
