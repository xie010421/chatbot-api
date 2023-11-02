package cn.chatbot.api.domain.zsxq;


import cn.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;

import java.io.IOException;

public interface IXzxApi {
    /**
     * 查询未回答的问题列表的TopicId
     * @param groupId  知识星球Id
     * @param cookie   返回cookie
     * @return
     */
    UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId,String cookie) throws IOException;

    /**
     * 返回结果
     * @param groupId
     * @param cookie
     * @param topicId
     * @param text
     * @param silenced
     * @return 是否success
     */
    Boolean answer(String groupId,String cookie,String topicId,String text,boolean silenced) throws IOException;
}
