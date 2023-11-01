package cn.chatbot.api.test;


import cn.chatbot.api.domain.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.chatbot.api.domain.model.vo.Topics;
import cn.chatbot.api.domain.services.XzxApi;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {
    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private XzxApi xzxApi;

    @Test
    public void test_XzxApi() throws IOException {
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = xzxApi.queryUnAnsweredQuestionsTopicId(groupId,cookie);
        logger.info("测试结果：{}",JSON.toJSONString(unAnsweredQuestionsAggregates));

        String text = "测试个够吧";
        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics
             ) {
            String topicId = topic.getTopic_id();
            boolean silenced = false;  //似乎是获取回答是否是私有化
            Boolean answer = xzxApi.answer(groupId, cookie, topicId, text, false);
            logger.info("topicId:{} anwer:{}" ,topicId,answer);

        }
    }

}
