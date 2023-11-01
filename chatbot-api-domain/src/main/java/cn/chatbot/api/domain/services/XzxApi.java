package cn.chatbot.api.domain.services;

import cn.chatbot.api.domain.IXzxApi;
import cn.chatbot.api.domain.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.chatbot.api.domain.model.req.AnswerReq;
import cn.chatbot.api.domain.model.req.ReqData;
import cn.chatbot.api.domain.model.res.AnswerRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class XzxApi implements IXzxApi {
    private Logger logger = LoggerFactory.getLogger(XzxApi.class);

    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建GET请求
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/"+groupId+"/topics?scope=unanswered_questions&count=20");
        //添加请求头
        get.addHeader("cookie",cookie);
        get.addHeader("Content-Type","application/json;charset=utf8");
        //封装响应数据
        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //将响应数据由JSON转为返回聚合类对象
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取问题数据：groupId:{}  jsonStr:{}",groupId,jsonStr);
            return JSON.parseObject(jsonStr,UnAnsweredQuestionsAggregates.class);
        }else {
            throw new RuntimeException("queryUnAnsweredQuestionsTopicId Err Code is" + response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public Boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        //1.提交表单 -- POST   //https://api.zsxq.com/v2/topics/411555812111558/answer
        // 此处的id是问题的topic_id,在获取问题的响应体中
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/"+topicId+"/answer");
        //2.添加请求头
        post.addHeader("cookie",cookie);
        post.addHeader("Content-Type","application/json, text/plain");
        //2.1表明这个请求是由浏览器发出
        post.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        //3.设置请求数据
        AnswerReq answerReq = new AnswerReq(new ReqData(text,silenced));
        String paramJson = JSONObject.toJSON(answerReq).toString();
        //3.1包装数据
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        //4.获取响应数据
        CloseableHttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //将响应数据由JSON转为字符串
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("回答结果数据：groupId:{} topicId:{} jsonStr:{}",groupId,topicId,jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
            return answerRes.isSucceeded();
        }else {
            throw new RuntimeException("answer Err Code is" + response.getStatusLine().getStatusCode() );
        }
    }

}
