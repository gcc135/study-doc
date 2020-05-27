package com.olichem.demo.controller.copy;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.olichem.demo.domain.Token;
import com.olichem.demo.domain.User;
import com.olichem.demo.utils.HttpClient;

/**
 * @Description: 第三方的微信登录demo
 * @author: 耿程程
 * @date: 2020-05-27 13:48:11
 */
@RequestMapping("/login")
@Controller
public class WeixinLoginController {
	
	/**
	 * @Description: 微信服务器回调操作
	 * 1. 微信服务器返回一个code
	 * 2. 根据code拼装请求,发送给微信服务器,获取access_token
	 * 3. 得到access_token,根据access_token拼装请求,发送给微信服务器,获取userinfo
	 * 4. 得到用户信息,进行数据存储和逻辑处理,并返回到前端展示
	 * @author: 耿程程 
	 * @date: 2020-05-27 14:11:49
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="weixin", method = { RequestMethod.GET })
	public String weixinLogin(HttpServletRequest request, ModelMap model) {
		System.out.println("微信服务器回调...");
		// 获取到code值
		String code = request.getParameter("code");
		// 判断传入参数
		if (code == null) {
			throw new RuntimeException("用户禁止授权...");
		}
		try {
			// 获取到了code值，回调没有问题
			// 定义地址
			String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx7287a60bb700fd21"
					+ "&secret=1ef8755f92bebae8ad7bab432ba29cbf&code=" + code + "&grant_type=authorization_code";
			// 向微信服务器发送请求
			HttpClient tokenClient = new HttpClient(tokenUrl);
			// 发送get请求
			tokenClient.get();
			// 获取到请求的结果 json格式的字符串，把json格式的字符串转换成对象或者Map集合
			String tokenContent = tokenClient.getContent();
			// 把json字符串转换成对象
			Token token = JSON.parseObject(tokenContent, Token.class);
			// 获取到接口调用凭证,根据调用凭证再次请求微信服务器获取用户信息
			// 获取个人的信息
			String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token.getAccess_token()
					+ "&openid=" + token.getOpenid();
			HttpClient userInfoClient = new HttpClient(userInfoUrl);
			userInfoClient.get();
			String userContent = userInfoClient.getContent();
			// 解析json字符串
			User user = JSON.parseObject(userContent, User.class);
			System.out.println("微信用户信息：" + user);
			// 业务逻辑 存储数据库 redis等操作
			model.addAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("微信扫描登录异常...");
		}
		return "index";
	}
}
