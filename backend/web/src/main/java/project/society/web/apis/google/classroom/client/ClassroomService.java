package project.society.web.apis.google.classroom.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import project.society.security.util.CustomOAuth2Util;
import reactor.core.publisher.Mono;

@Service
public class ClassroomService {
    private final CustomOAuth2Util oAuth2Util;
    private final WebClient client;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClassroomService(@Qualifier("classroomWebClient") WebClient client, CustomOAuth2Util oAuth2Util) {
        this.client = client;
        this.oAuth2Util = oAuth2Util;
    }

    /**
     * Wrapper function for {@link #getResponseMono(String, String, Class)}
     * @param path Relative path to classroom api endpoint.
     * @param request Current {@link ServerRequest}.
     * @param clazz {@link Class} we want to map response to.
     * @param <T> Type we want response mapped to.
     * @return {@link Mono} of {@link T}.
     */
    public <T> Mono<T> getResponseMono(String path, ServerRequest request, Class<T> clazz) {
        return oAuth2Util.extractAccessToken(request).flatMap(token -> getResponseMono(path, token, clazz));
    }

    /**
     * Sends a request to a path on the classroom api, and returns the response.
     * @param path Relative path to classroom api endpoint.
     * @param token Access token.
     * @param clazz {@link Class} we want to map response to.
     * @param <T> Type we want response mapped to.
     * @return {@link Mono} of {@link T}.
     */
    public <T> Mono<T> getResponseMono(String path, String token, Class<T> clazz) {
        String newRequestDebugStr = "New Request " + path;
        logger.debug("\n" + "|".repeat(newRequestDebugStr.length()) +
                "\n" + newRequestDebugStr + "\n" + "V".repeat(newRequestDebugStr.length()));
        return client.get().uri(path)
                .header("Authorization", "Bearer " + token)
                .exchangeToMono(clientResponse -> {
                    String responseDebugStr =
                            "uri: " + path + ", Status Code: " + clientResponse.statusCode() +
                                    ", Token: " + token.startsWith("ya29");
                    logger.debug("\n" + "-".repeat(responseDebugStr.length()) +
                            "\n" + responseDebugStr + "\n" + "-".repeat(responseDebugStr.length()));
                    return clientResponse.bodyToMono(clazz);
                });
    }
}
