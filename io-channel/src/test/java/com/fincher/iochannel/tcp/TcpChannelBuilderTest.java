package com.fincher.iochannel.tcp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fincher.iochannel.IoType;

import org.junit.Test;

public class TcpChannelBuilderTest {

    @Test
    public void test() {
        TcpSocketOptions options = new TcpSocketOptions();
        ConnectionEstablishedListener listener1 = (id) -> id.toString();
        ConnectionEstablishedListener listener2 = (id) -> id.toString();
        StreamIo streamIo = mock(StreamIo.class);
        
        TestBuilder builder = new TestBuilder()
        .withId("testId")
        .withIoType(IoType.INPUT_AND_OUTPUT)
        .withSocketOptions(options)
        .addConnectionEstablishedListener(listener1)
        .addConnectionEstablishedListener(listener2)
        .withStreamIo(streamIo);
        
        assertEquals(options, builder.getSocketOptions().get());
        assertEquals(2, builder.getConnectionEstablishedListeners().size());
        assertEquals(streamIo, builder.getStreamIo().get());
        
        TcpServerChannel channel = builder.build();
        
        verify(channel, times(1)).setSocketOptions(options);
        verify(channel, times(1)).addConnectionEstablishedListener(listener1);
        verify(channel, times(1)).addConnectionEstablishedListener(listener2);
    }
    
    @Test
    public void testWithNoSocketOptionsAndListener() {
        TestBuilder builder = new TestBuilder();
        builder.withId("testId");
        builder.withIoType(IoType.INPUT_AND_OUTPUT);
        TcpServerChannel channel = builder.build();
        
        verify(channel, never()).setSocketOptions(any());
        verify(channel, never()).addConnectionEstablishedListener(any());
    }
    

    private static class TestBuilder extends TcpChannelBuilder<TcpServerChannel, TestBuilder> {
        
        public TcpServerChannel channel = mock(TcpServerChannel.class);
        
        @Override
        public TcpServerChannel doBuild() {
            return channel;
        }
    }

}
