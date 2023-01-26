import com.nordlund.app.Application;
import com.nordlund.app.core.model.ErrorMessages;
import com.nordlund.app.service.model.JsonMessage;
import io.restassured.http.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MessagingServiceTest {

    @LocalServerPort
    private int port;

    @Test
    void addMessage() {
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(null, "this is a test value", "abc123"))
                .when()
                .post("/messaging-service/messages")
                .then()
                .statusCode(200)
                .assertThat()
                .body("messageContent", equalTo("this is a test value"))
                .body("id", equalTo(1))
                .body("userId", equalTo("abc123"));
    }

    @Test
    void addMessageSuppliedIdIsIgnored() {
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(6L, "asbd", "abc123"))
                .when()
                .post("/messaging-service/messages")
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(1));
    }

    @Test
    void updateMessage() {
        addMessage();
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(1L, "this is a test value updated", "abc123"))
                .when()
                .put("/messaging-service/messages/" + 1)
                .then()
                .statusCode(200)
                .assertThat()
                .body("messageContent", equalTo("this is a test value updated"))
                .body("id", equalTo(1))
                .body("userId", equalTo("abc123"));
    }

    @Test
    void updateMessageNotAllowed() {
        addMessage();
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(1L, "this is a test value updated", "xyz123"))
                .when()
                .put("/messaging-service/messages/" + 1)
                .then()
                .statusCode(401)
                .assertThat()
                .body("message", equalTo(ErrorMessages.USER_NOT_AUTHORIZED));
    }

    @Test
    void updateMessageDoesntExist() {
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(1L, "this is a test value updated", "abc123"))
                .when()
                .put("/messaging-service/messages/" + 1)
                .then()
                .statusCode(404)
                .assertThat()
                .body("message", equalTo(String.format(ErrorMessages.MESSAGE_NOT_FOUND, 1L)));
    }

    @Test
    void deleteMessage() {
        addMessage();
        with().port(port).contentType(ContentType.JSON)
                .when()
                .delete("/messaging-service/messages/1/abc123")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteMessageDoesntExist() {
        addMessage();
        with().port(port).contentType(ContentType.JSON)
                .when()
                .delete("/messaging-service/messages/2/abc123")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteMessageNotAllowed() {
        addMessage();
        with().port(port).contentType(ContentType.JSON)
                .when()
                .delete("/messaging-service/messages/1/xyz123")
                .then()
                .statusCode(401)
                .assertThat()
                .body("message", equalTo(ErrorMessages.USER_NOT_AUTHORIZED));
    }

    @Test
    void getMessages() {
        ParameterizedTypeReference<List<JsonMessage>> typeReference = new ParameterizedTypeReference<>() {
        };
        addMessageNoAssertions(1);
        addMessageNoAssertions(2);
        addMessageNoAssertions(3);
        List<JsonMessage> messages = with().port(port).contentType(ContentType.JSON)
                .when()
                .get("/messaging-service/messages")
                .then()
                .statusCode(200)
                .extract().as(typeReference.getType());
        Assertions.assertEquals(3, messages.size());
        assertMessage(messages.get(0), 1);
        assertMessage(messages.get(1), 2);
        assertMessage(messages.get(2), 3);
    }

    @Test
    void getMessagesNoneExist() {
        ParameterizedTypeReference<List<JsonMessage>> typeReference = new ParameterizedTypeReference<>() {
        };
        List<JsonMessage> messages = with().port(port).contentType(ContentType.JSON)
                .when()
                .get("/messaging-service/messages")
                .then()
                .statusCode(200)
                .extract().as(typeReference.getType());
        Assertions.assertEquals(0, messages.size());
    }

    private void addMessageNoAssertions(int i) {
        with().port(port).contentType(ContentType.JSON)
                .body(new JsonMessage(null, "this is a test value " + i, "abc123"))
                .when()
                .post("/messaging-service/messages")
                .then()
                .statusCode(200);
    }

    private void assertMessage(@NotNull final JsonMessage message, int i) {
        Assertions.assertEquals("this is a test value " + i, message.getMessageContent());
        Assertions.assertEquals(i, message.getId());
        Assertions.assertEquals("abc123", message.getUserId());
    }
}
