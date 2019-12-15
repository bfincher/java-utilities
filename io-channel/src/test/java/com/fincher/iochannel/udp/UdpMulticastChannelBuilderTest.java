package com.fincher.iochannel.udp;

import static org.junit.Assert.assertEquals;

import com.fincher.iochannel.IoChannelBuilder;
import com.fincher.iochannel.IoType;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Test;

public class UdpMulticastChannelBuilderTest {
    
    private static InetAddress mcAddress;
    private static InetSocketAddress remoteAddress;
    
    @BeforeClass
    public static void beforeClass() throws UnknownHostException {
        mcAddress = InetAddress.getByName("239.0.0.1");
        remoteAddress = new InetSocketAddress(mcAddress, 1234);
    }
    
    @Test
    public void testOutputChannel() {
        InetSocketAddress localAddress = new InetSocketAddress(1234);
        UdpMulticastSocketOptions options = new UdpMulticastSocketOptions();
        options.setReceiveBufferSize(1);
        
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY)
                .atLocalAddress(localAddress)
                .sendingToRemoteAddress(remoteAddress)
                .withSocketOptions(options);
        
        UdpMulticastChannel channel = builder.build();
        assertEquals(options, builder.getSocketOptions().get());
        assertEquals(localAddress, channel.getlocalAddress());
        assertEquals(remoteAddress, channel.remoteAddress);
        assertEquals(options, channel.getSocketOptions());
    }
    
    @Test
    public void testInputChannel() {
        InetSocketAddress localAddress = new InetSocketAddress(1234);
        UdpMulticastSocketOptions options = new UdpMulticastSocketOptions();
        options.setReceiveBufferSize(1);
        
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
                .withId("testId")
                .withIoType(IoType.INPUT_ONLY)
                .atLocalAddress(localAddress)
                .receivingFromMulticastAddress(mcAddress)
                .withSocketOptions(options);
        
        UdpMulticastChannel channel = builder.build();
        assertEquals(options, builder.getSocketOptions().get());
        assertEquals(localAddress, channel.getlocalAddress());
        assertEquals(mcAddress, builder.getMulticastAddress().get());
        assertEquals(mcAddress, channel.getMulticastAddress());
        assertEquals(options, channel.getSocketOptions());
    }
    
    @Test
    public void testDefaultLocalAddress() {
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
        .withId("testId")
        .sendingToRemoteAddress(remoteAddress)
        .withIoType(IoType.OUTPUT_ONLY);
        
        UdpMulticastChannel channel = builder.build();
        assertEquals(new InetSocketAddress(0), channel.getlocalAddress());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithInputAndOutput() {
        IoChannelBuilder.createForMulticast()
                .withIoType(IoType.INPUT_AND_OUTPUT);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoRemoteAddressOnOutput() {
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
                .withId("testId")
                .withIoType(IoType.OUTPUT_ONLY);
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemoteAddressOnInput() {
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
                .withId("testId")
                .withIoType(IoType.INPUT_ONLY)
                .sendingToRemoteAddress(remoteAddress);
        builder.build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNoMulticastAddressOnInput() {
        UdpMulticastChannelBuilder builder = IoChannelBuilder.createForMulticast()
                .withId("testId")
                .withIoType(IoType.INPUT_ONLY);
        builder.build();
    }

}
