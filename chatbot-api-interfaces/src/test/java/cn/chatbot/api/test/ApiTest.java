package cn.chatbot.api.test;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import sun.net.www.http.HttpClient;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;

public class ApiTest {
    /**
     * 获取提问请求数据
     * @throws IOException
     */
    @Test
    void unAnswer_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建GET请求
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/51112814845484/topics?scope=unanswered_questions&count=20");
        //添加请求头
        get.addHeader("cookie","zsxq_access_token=BEE72A12-88D6-C7C4-8109-2FAC72616544_DFBEF78A365333B7; zsxqsessionid=fd3d24f73bd49e9dc114f2a39ece9eb0; abtest_env=product; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22415844255444888%22%2C%22first_id%22%3A%2218b8a3d26f011db-07a46a7938f5894-26031151-1327104-18b8a3d26f16c2%22%2C%22props%22%3A%7B%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiOGEzZDI2ZjAxMWRiLTA3YTQ2YTc5MzhmNTg5NC0yNjAzMTE1MS0xMzI3MTA0LTE4YjhhM2QyNmYxNmMyIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiNDE1ODQ0MjU1NDQ0ODg4In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22415844255444888%22%7D%2C%22%24device_id%22%3A%2218b8a3d26f011db-07a46a7938f5894-26031151-1327104-18b8a3d26f16c2%22%7D");
        get.addHeader("Content-Type","application/json, text/plain");
        //封装响应数据
        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //将响应数据由JSON转为字符串
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else {
            //输出错误状态码
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }


    /**
     * 返回回答问题数据
     */
    @Test
    void answer() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        //1.提交表单 -- POST   //https://api.zsxq.com/v2/topics/411555812111558/answer
        // 此处的id是问题的topic_id,在获取问题的响应体中
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/588555188121214/answer");
        //2.添加请求头
        post.addHeader("cookie","zsxq_access_token=BEE72A12-88D6-C7C4-8109-2FAC72616544_DFBEF78A365333B7; zsxqsessionid=fd3d24f73bd49e9dc114f2a39ece9eb0; abtest_env=product; sajssdk_2015_cross_new_user=1; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22415844255444888%22%2C%22first_id%22%3A%2218b8a3d26f011db-07a46a7938f5894-26031151-1327104-18b8a3d26f16c2%22%2C%22props%22%3A%7B%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMThiOGEzZDI2ZjAxMWRiLTA3YTQ2YTc5MzhmNTg5NC0yNjAzMTE1MS0xMzI3MTA0LTE4YjhhM2QyNmYxNmMyIiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiNDE1ODQ0MjU1NDQ0ODg4In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22415844255444888%22%7D%2C%22%24device_id%22%3A%2218b8a3d26f011db-07a46a7938f5894-26031151-1327104-18b8a3d26f16c2%22%7D");
        post.addHeader("Content-Type","application/json, text/plain");
        //3.设置请求数据
        String paramJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"测试个屁\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"silenced\": true\n" +
                "  }\n" +
                "}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        //4.获取响应数据
        CloseableHttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //将响应数据由JSON转为字符串
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else {
            //输出错误状态码
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }



}
