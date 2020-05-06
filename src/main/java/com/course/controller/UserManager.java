package com.course.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.course.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@Api(value="v1" ,description="�û�����ϵͳ")
@RequestMapping("v1")
public class UserManager {
	
	@Autowired
	private SqlSessionTemplate template;
	
	@ApiOperation(value="��¼�ӿ�",httpMethod="POST")
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Boolean login(HttpServletResponse response,
							@RequestBody User user){
		int i = template.selectOne("login",user);
		Cookie cookie = new Cookie("login", "true");
		response.addCookie(cookie);
		log.info("��ѯ���Ľ����"+i);
		if(i==1){
			log.info("��¼���û��ǣ�"+user.getUsername());
			return true;
		}
		
		return false;		
	}
	
	@ApiOperation(value="����û��ӿ�",httpMethod="POST")
	@RequestMapping(value="addUser",method=RequestMethod.POST)
	public boolean addUser(HttpServletRequest request,@RequestBody User user){
		Boolean x = verifyCookies(request);
		int result = 0;
		if (x!=null){
			result = template.insert("addUser",user);
		}
		if(result>0){
			log.info("����û��������ǣ�"+result);
			return true;
		}				
		return false;
		
	}
	
	@ApiOperation(value="��ȡ�û����б���Ϣ",httpMethod="POST")
	@RequestMapping(value="getUserInfo",method=RequestMethod.POST)
	public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
		Boolean x = verifyCookies(request);
		if(x==true){
			List<User> users = template.selectList("getUserInfo",user);
			log.info("getUserInfo��ȡ�����û�������"+users.size());
			return users;
		}else{
			return null;
		}	
	}
	
	@ApiOperation(value="����/ɾ���û��ӿ�",httpMethod="POST")
	@RequestMapping(value="/updateUserInfo",method=RequestMethod.POST)
	public int updateUser(HttpServletRequest request,@RequestBody User user){
		
		Boolean x = verifyCookies(request);
		int i=0;
		if(x==true){
			i = template.update("updateUserInfo",user);		
		}
		log.info("�������ݵ�����Ϊ��"+i);
		return i;
		
	}

	private Boolean verifyCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(Objects.isNull(cookies)){
			log.info("cookiesΪ��");
			return false;
		}
		for(Cookie cookie:cookies){
			if(cookie.getName().equals("login") && cookie.getValue().equals("true")){
				log.info("cookies��֤ͨ��");
				return true;
			}
		}
		return false;
	}

}
