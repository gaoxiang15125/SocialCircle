package github.zlg.socialcircle.server.service;

import com.alibaba.fastjson.JSON;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.mapper.socialcircle.SocialCircleMapper;
import org.junit.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 社交圈圈数据库操作测试代码
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-09 14:52
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SocialCircleMapperTest {

    @Autowired
    SocialCircleMapper socialCircleMapper;

    @Test
    public void testDistanceNum() {

        int num = socialCircleMapper.getDistanceSocialCircleNum(101,22,2000);
        System.out.println(num);
    }

    @Test
    public void testDistance() {
        List<SocialCircle> resultList = socialCircleMapper.getDistanceSocialCircleByPage(101,22,2000, 0,10);
        System.out.println(JSON.toJSONString(resultList));
    }
}
