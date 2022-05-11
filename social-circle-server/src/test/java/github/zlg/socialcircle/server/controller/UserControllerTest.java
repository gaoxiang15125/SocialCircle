//package github.zlg.socialcircle.server.controller;
//
//import com.alibaba.fastjson.JSON;
//import github.zlg.socialcircle.module.dto.user.WeiXinUserDto;
//import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
///**
// * @program: social-circle-main
// * @description:
// * @author: gaoxiang
// * @email: 630268696@qq.com
// * @create: 2021-04-01 16:15
// **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserControllerTest {
//
//    @Autowired
//    UserController userController;
//
//    @Test
//    public void login() {
//        userController.login("randomCode");
//    }
//
//    @Test
//    public void addUserInfo() {
//        WeiXinUserDto weiXinUser = new WeiXinUserDto();
////        weiXinUser.setOpenid("circleOfLife");
//        weiXinUser.setNickName("生命的循环");
//        weiXinUser.setAvatarUrl("avactor.png");
//        weiXinUser.setGender(0);
//        weiXinUser.setCountry("中国");
//        weiXinUser.setProvince("河南");
//        weiXinUser.setCity("郑州");
//        weiXinUser.setLanguage("简体中文");
//        System.out.println(JSON.toJSONString(weiXinUser));
////        userController.addUserInfo("circleOfLife",weiXinUser);
//    }
//
//    @Test
//    public void getUserInfo() {
//    }
//
//    @Test
//    public void updateUserInfo() {
//    }
//
//    @Test
//    public void getCollectSocialCircle() {
//    }
//
//    @Test
//    public void getCollectSocialCircleNum() {
//    }
//
//    @Test
//    public void getCollectModuleContent() {
//    }
//
//    @Test
//    public void getCollectModuleContentNum() {
//    }
//
//    @Test
//    public void cancelCollectSocialCircle() {
//    }
//
//    @Test
//    public void cancelCollectModuleContent() {
//    }
//}