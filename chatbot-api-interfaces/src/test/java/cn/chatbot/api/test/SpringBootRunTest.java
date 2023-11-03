package cn.chatbot.api.test;


import cn.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.chatbot.api.domain.zsxq.model.vo.Topics;
import cn.chatbot.api.domain.zsxq.services.XzxApi;
import cn.chatbot.api.domain.gptai.service.OpenAI;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Resource
    private OpenAI openAI;
    @Test
    public void test_XzxApi() throws IOException {
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = xzxApi.queryUnAnsweredQuestionsTopicId(groupId,cookie);
        logger.info("测试结果：{}",JSON.toJSONString(unAnsweredQuestionsAggregates));

        String text = "测试个够吧";


        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics
             ) {
            String topicId = topic.getTopic_id();
            //String text = openAI.doChatGPT(topic.getQuestion().getText());
            boolean silenced = false;  //似乎是获取回答是否是私有化
            Boolean answer = xzxApi.answer(groupId, cookie, topicId, text, false);
            logger.info("topicId:{} anwer:{}" ,topicId,answer);

        }
    }

    @Test
    public void testOpenApi() throws IOException {
        String answer = openAI.doChatGPT("请使用java帮我写一个冒泡排序算法！");
        System.out.println(answer);
    }
}
