package com.fincher.iochannel.tcp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.fincher.iochannel.IoChannelBuilder;
import com.fincher.iochannel.IoType;

import java.net.InetSocketAddress;

import org.junit.Test;

public class TcpClientChannelBuilderTest {
    
    @Test
    public void test() {
        TcpSocketOptions options = new TcpSocketOptions();
        StreamIo streamIo = mock(StreamIo.class);
        InetSocketAddress localAddress = new InetSocketAddress("localhost", 1234);
        InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 2345);
        
        TcpClientChannelBuilder builder = IoChannelBuilder.createForTcpClient()
        .withId("testId")
        .withIoType(IoType.OUTPUT_ONLY)
        .atLocalAddress(localAddress)
        .withRemoteAddress(remoteAddress)
        .withSocketOptions(options)
        .withStreamIo(streamIo);
        
        TcpClientChannel channel = builder.build();
        assertEquals(localAddress, channel.getlocalAddress());
        assertEquals(options, channel.getSocketOptions());
    }
    
    @Test
    public void testDefaultLocalAddress() {
        StreamIo streamIo = mock(StreamIo.class);
        
        TcpClientChannelBuilder builder = IoChannelBuilder.createForTcpClient()
        .withId("testId")
        .withIoType(IoType.OUTPUT_ONLY)
        .withStreamIo(streamIo)
        .withRemoteAddress(mock(InetSocketAddress.class));
        
        TcpClientChannel channel = builder.build();
        assertEquals(new InetSocketAddress(0), channel.getlocalAddress());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoStreamIo() {
        TcpClientChannelBuilder builder = IoChannelBuilder.createForTcpClient()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY)
                .withRemoteAddress(mock(InetSocketAddress.class));
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoRemoteAddress() {
        TcpClientChannelBuilder builder = IoChannelBuilder.createForTcpClient()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY)
                .withStreamIo(mock(StreamIo.class));
        builder.build();
    }

}
