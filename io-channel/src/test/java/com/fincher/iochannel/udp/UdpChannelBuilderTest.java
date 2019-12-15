package com.fincher.iochannel.udp;

import static org.junit.Assert.assertEquals;

import com.fincher.iochannel.IoChannelBuilder;
import com.fincher.iochannel.IoType;

import java.net.InetSocketAddress;

import org.junit.Test;

public class UdpChannelBuilderTest {
    
    @Test
    public void test() {
        InetSocketAddress localAddress = new InetSocketAddress(1234);
        InetSocketAddress remoteAddress = new InetSocketAddress(2345);
        UdpSocketOptions options = new UdpSocketOptions();
        options.setReceiveBufferSize(1);
        
        UdpChannelBuilder builder = IoChannelBuilder.createForUnicast()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY)
                .atLocalAddress(localAddress)
                .sendingToRemoteAddress(remoteAddress)
                .withSocketOptions(options);
        
        UdpChannel channel = builder.build();
        assertEquals(localAddress, channel.getlocalAddress());
        assertEquals(remoteAddress, channel.remoteAddress);
        assertEquals(options, channel.getSocketOptions());
    }
    
    @Test
    public void testDefaultLocalAddress() {
        UdpChannelBuilder builder = IoChannelBuilder.createForUnicast()
        .withId("testId")
        .withIoType(IoType.INPUT_ONLY);
        
        UdpChannel channel = builder.build();
        assertEquals(new InetSocketAddress(0), channel.getlocalAddress());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithInputAndOutput() {
        IoChannelBuilder.createForUnicast()
                .withIoType(IoType.INPUT_AND_OUTPUT);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoRemoteAddressOnOutput() {
        UdpChannelBuilder builder = IoChannelBuilder.createForUnicast()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY);
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemoteAddressOnInput() {
        UdpChannelBuilder builder = IoChannelBuilder.createForUnicast()
                .withId("testId")
                .withIoType(IoType.INPUT_ONLY)
                .sendingToRemoteAddress(new InetSocketAddress(0));
        builder.build();
    }

}
