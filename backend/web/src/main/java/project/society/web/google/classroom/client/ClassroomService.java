package project.society.web.google.classroom.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import project.society.security.util.CustomOAuth2Util;
import project.society.web.google.classroom.ClassroomResponseType;
import reactor.core.publisher.Mono;

@Service
public class ClassroomService {
    private final CustomOAuth2Util oAuth2Utils;
    private final WebClient client;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ClassroomService(CustomOAuth2Util oAuth2Utils, @Qualifier("classroomWebClient") WebClient client) {
        this.oAuth2Utils = oAuth2Utils;
        this.client = client;
    }

    /**
     * Wrapper function for {@link #getResponseMono(String, String, Class)}
     * @param path Target classroom uri.
     * @param request Incoming {@link ServerRequest}.
     * @param clazz Instance of {@link Class} to map response to.
     * @param <T> {@link Class} to map response to.
     * @return {@link Mono} of {@link T}.
     */
    public <T extends ClassroomResponseType> Mono<T> getResponseMono(String path, ServerRequest request, Class<T> clazz) {
        return oAuth2Utils.extractAccessToken(request).flatMap(token -> getResponseMono(path, token, clazz));
    }

    /**
     * Sends a request to a classroom url and returns the response as a mono.
     * @param path Target classroom uri.
     * @param token End user access token.
     * @param clazz Instance of {@link Class} to map response to.
     * @param <T> {@link Class} to map response to.
     * @return {@link Mono} of {@link T}.
     */
    public <T extends ClassroomResponseType> Mono<T> getResponseMono(String path, String token, Class<T> clazz) {
            String newRequestDebugStr = "New Request " + path;
            log.debug("\n" + "|".repeat(newRequestDebugStr.length()) + "\n" + newRequestDebugStr + "\n" + "V".repeat(newRequestDebugStr.length()));
            return client.get().uri(path)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .exchangeToMono(clientResponse -> {
                        String responseDebugStr =
                                "uri: " + path + ", Status Code: " + clientResponse.statusCode() + ", Token: " + token.startsWith("ya29");
                        log.debug("\n" + "-".repeat(responseDebugStr.length()) + "\n" + responseDebugStr + "\n" + "-".repeat(responseDebugStr.length()));
                        return clientResponse.bodyToMono(clazz);
                    });
    }
}
