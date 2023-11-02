package cn.chatbot.api.domain.gptai.service;

import cn.chatbot.api.domain.gptai.IOpenAI;
import cn.chatbot.api.domain.gptai.model.aggregates.AIAnswer;
import cn.chatbot.api.domain.gptai.model.vo.Choices;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class OpenAI implements IOpenAI {
    private Logger logger = LoggerFactory.getLogger(OpenAI.class);

    @Value("${chatbot-api.openAiKey}")
    private String openAiKey;

    @Override
    public String doChatGPT(String question) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
        //HttpPost post = new HttpPost("https://api.openai.com/v1/completions");
        post.addHeader("Content-Type","application/json");
        post.addHeader("Authorization","Bearer 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");
        String paramJson = "{\n" +
                "     \"model\": \"gpt-3.5-turbo\",\n" +
                "     \"messages\": [{\"role\": \"user\", \"content\": \"请帮我写一个冒泡排序！\"}],\n" +
                "     \"temperature\": 0.7\n" +
                "   }";
        //String paramJson = "{\"model\": \"text-davinci-003\", \"prompt\": \"帮我写一个java冒泡排序\", \"temperature\": 0, \"max_tokens\": 1024}";
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        //4.获取响应数据
        CloseableHttpResponse response = client.execute(post);
        //测试
        /*System.out.println(response.getStatus());
        System.out.println(response.body());*/
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //将响应数据由JSON转为字符串
            String jsonStr = EntityUtils.toString(response.getEntity());
            AIAnswer answer = JSON.parseObject(jsonStr,AIAnswer.class);
            List<Choices> choices = answer.getChoices();
            StringBuilder anwers = new StringBuilder();
            for (Choices choice: choices
                 ) {
                anwers.append(choice.getText());
            }
            return anwers.toString();
        }else {
            throw new RuntimeException("api.openai.com Err Code is" + response.getStatusLine().getStatusCode());
        }
    }

}
