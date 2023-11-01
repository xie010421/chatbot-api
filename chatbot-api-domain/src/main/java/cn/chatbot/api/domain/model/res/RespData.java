package cn.chatbot.api.domain.model.res;

import cn.chatbot.api.domain.model.vo.Topics;

import java.util.List;

public class RespData {
    private List<Topics> topics;

    public void setTopics(List<Topics> topics){
        this.topics = topics;
    }
    public List<Topics> getTopics(){
        return this.topics;
    }
}
