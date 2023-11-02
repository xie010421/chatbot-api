package cn.chatbot.api.domain.gptai;

import java.io.IOException;

public interface IOpenAI {
    String doChatGPT(String question) throws IOException;
}
