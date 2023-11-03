package cn.chatbot.api.application.job;


import cn.chatbot.api.domain.gptai.service.OpenAI;
import cn.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.chatbot.api.domain.zsxq.model.vo.Topics;
import cn.chatbot.api.domain.zsxq.services.XzxApi;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

//轮循执行查询问题，回答问题任务
@EnableScheduling
@Configuration
public class ChatBotSchedule {
    private Logger logger = LoggerFactory.getLogger(ChatBotSchedule.class);

    @Value("${chatbot-api.cookie}")
    private String cookie;
    @Value("${chatbot-api.groupId}")
    private String groupId;

    @Resource
    private OpenAI openAI;
    @Resource
    private XzxApi xzxApi;

    //表达式:cron.qqe2.com
    @Scheduled(cron = "0/30 * * * * ?") //30秒轮循一次
    public void run(){
        //由于有规律的轮循，有可能会被风控，所示随机轮循，避免风控 -- 随机打烊
        if (new Random().nextBoolean()){
            logger.info("随机打烊中...");
            return;
        }
        //模拟人类作息
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 22 || hour < 7){
            logger.info("打烊时间，休息中...");
            return;
        }
        try {
            //1.0获取问题列表
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = xzxApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("拉取问题结果数据：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (topics == null || topics.isEmpty()){
                logger.info("此次未检索到任何问题！");
                return;
            }

            //2.0AI回答
            //2.1每次回答获取到的第一个问题
            Topics topics1 = topics.get(0);
            String question = topics1.getQuestion().getText();
            logger.info("问题：{}",question);
            String anwser = openAI.doChatGPT(question);

            //3.0将回答的问题回复给提问者
            Boolean status = xzxApi.answer(groupId, cookie, topics1.getTopic_id(), anwser, false);
            logger.info("编号：{} 问题：{} 回答：{} 状态：{}",topics1.getTopic_id(),question,anwser,status);
        } catch (IOException e) {
            logger.info("自动轮循回答问题出现异常！");
        }
    }
}
