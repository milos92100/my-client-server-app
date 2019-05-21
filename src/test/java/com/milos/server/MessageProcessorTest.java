package com.milos.server;

import com.milos.MockedLogger;
import com.milos.domain.Answer;
import com.milos.domain.Message;

import junit.framework.TestCase;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class MessageProcessorTest extends TestCase {

    public MessageProcessorTest(String testName) {
        super(testName);
    }


    public void testMessageReceivedShouldReturnRejectedForInvalidMessage() {
        String invalidType = "foo";

        Message givenMessage = new Message(UUID.randomUUID(), new Date(), invalidType, "foo_payload");

        MessageProcessor processor = new MessageProcessor(new MockedLogger(), new MessageStore());
        Answer actual = processor.messageReceived(givenMessage);

        assertTrue(actual.messageWas(Answer.Type.REJECTED));
    }

    public void testMessageReceivedReturnsNotProcessedDueFailure() throws Exception {
        Message givenMessage = new Message(UUID.randomUUID(), new Date(), Message.Type.ACCOUNT_CHANGE, "foo_payload");

        MessageProcessor processor = new MessageProcessor(new MockedLogger(), new MessageStore());
        MessageProcessor processorSpy = Mockito.spy(processor);

        doThrow(new InterruptedException("test")).when(processorSpy).process(givenMessage);

        Answer actual = processorSpy.messageReceived(givenMessage);

        assertTrue(actual.messageWas(Answer.Type.NOT_PROCESSED_DUE_FAILURE));
    }

    public void testMessageReceiveReturnsAcknowledged() {
        Message givenMessage = new Message(UUID.randomUUID(), new Date(), Message.Type.ACCOUNT_CHANGE, "foo_payload");
        MessageProcessor processor = new MessageProcessor(new MockedLogger(), new MessageStore());

        Answer actual = processor.messageReceived(givenMessage);

        assertTrue(actual.messageWas(Answer.Type.ACKNOWLEDGED));
    }

    public void testMessageIsPutOnQueue() {
        MessageStore store = new MessageStore();

        Message accountMessage = new Message(UUID.randomUUID(), new Date(), Message.Type.ACCOUNT_CHANGE, "foo_payload");
        Message userMessage = new Message(UUID.randomUUID(), new Date(), Message.Type.USER_CHANGE, "bar_payload");

        MessageProcessor processor = new MessageProcessor(new MockedLogger(), store);

        Answer accountAnswer = processor.messageReceived(accountMessage);
        Message actualAccountMessage = store.getAccountMessagesQueue().poll();

        assertTrue(accountAnswer.messageWas(Answer.Type.ACKNOWLEDGED));
        assertTrue(actualAccountMessage.equals(accountMessage));

        Answer userAnswer = processor.messageReceived(userMessage);
        Message actualUserMessage = store.getUserMessagesQueue().poll();

        assertTrue(userAnswer.messageWas(Answer.Type.ACKNOWLEDGED));
        assertTrue(actualUserMessage.equals(userMessage));
    }
}
