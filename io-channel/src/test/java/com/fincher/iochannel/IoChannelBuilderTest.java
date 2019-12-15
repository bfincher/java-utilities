package com.fincher.iochannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fincher.iochannel.udp.UdpChannel;

import java.util.function.Consumer;

import org.junit.Test;
import org.mockito.Mockito;

public class IoChannelBuilderTest {
    
    @Test
    public void test() {
        TestBuilder builder = new TestBuilder();
        
        // test null ID
        try {
            builder.withId(null);
            fail("Should have got exception");
        } catch (NullPointerException e) {
            // expected
        }
        
        // test good ID
        builder.withId("testId");
        assertEquals("testId", builder.getId());
        
        // test with null io type
        try {
            builder.withIoType(null);
            fail("Should have got exception");
        } catch (NullPointerException e) {
            // expected
        }
        
        // test with good io type
        builder.withIoType(IoType.INPUT_ONLY);
        assertEquals(IoType.INPUT_ONLY, builder.getIoType());
        
        Consumer<MessageBuffer> listener = (mb) -> mb.toString();
        builder.withMessageListener(listener);
        assertEquals(listener, builder.getMessageListeners().get(0));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildWithNoId() {
        TestBuilder builder = new TestBuilder();
        builder.withIoType(IoType.OUTPUT_ONLY);
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildWithNoIoType() {
        TestBuilder builder = new TestBuilder();
        builder.withId("testId");
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testListenerOnOutputOnly() {
        TestBuilder builder = new TestBuilder();
        builder.withId("testId")
        .withIoType(IoType.OUTPUT_ONLY)
        .withMessageListener(MessageBuffer::toString)
        .build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testBuildingTwice() {
        TestBuilder builder = new TestBuilder();
        builder.withId("testId");
        builder.withIoType(IoType.INPUT_AND_OUTPUT);
        builder.build();
        builder.build();
    }
    
    private static class TestBuilder extends IoChannelBuilder<MessageBuffer, UdpChannel, TestBuilder> {
        
        @Override
        public UdpChannel doBuild() {
            return Mockito.mock(UdpChannel.class);
        }
        
    }

}
