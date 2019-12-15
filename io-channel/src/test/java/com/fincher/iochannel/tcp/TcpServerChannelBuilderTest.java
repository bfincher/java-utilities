package com.fincher.iochannel.tcp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.fincher.iochannel.IoChannelBuilder;
import com.fincher.iochannel.IoType;

import java.net.InetSocketAddress;

import org.junit.Test;

public class TcpServerChannelBuilderTest {
    
    @Test
    public void test() {
        TcpSocketOptions options = new TcpSocketOptions();
        StreamIo streamIo = mock(StreamIo.class);
        InetSocketAddress localAddress = new InetSocketAddress("localhost", 1234);
        
        TcpServerChannelBuilder builder = IoChannelBuilder.createForTcpServer()
        .withId("testId")
        .withIoType(IoType.OUTPUT_ONLY)
        .atLocalAddress(localAddress)
        .withSocketOptions(options)
        .withStreamIo(streamIo);
        
        TcpServerChannel channel = builder.build();
        assertEquals(localAddress, channel.getlocalAddress());
        assertEquals(options, channel.getSocketOptions());
    }
    
    @Test
    public void testDefaultLocalAddress() {
        StreamIo streamIo = mock(StreamIo.class);
        
        TcpServerChannelBuilder builder = IoChannelBuilder.createForTcpServer()
        .withId("testId")
        .withIoType(IoType.OUTPUT_ONLY)
        .withStreamIo(streamIo);
        
        TcpServerChannel channel = builder.build();
        assertEquals(new InetSocketAddress(0), channel.getlocalAddress());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoStreamIo() {
        TcpServerChannelBuilder builder = (TcpServerChannelBuilder)IoChannelBuilder.createForTcpServer()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY);
        builder.build();
    }

}
