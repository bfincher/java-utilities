package com.fincher.iochannel.tcp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fincher.iochannel.ChannelException;
import com.fincher.iochannel.TestAnswer;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.Test;

public class TcpSocketOptionsTest {
    
    @Test
    public void testSocketNotSet() throws Exception {
        Socket socket = mock(Socket.class);
        TcpSocketOptions so = new TcpSocketOptions();
        so.clearTimeout();
        so.applySocketOptions("testId", socket);
        verify(socket, never()).setSendBufferSize(anyInt());
        verify(socket, never()).setReceiveBufferSize(anyInt());
        verify(socket, never()).setSoTimeout(anyInt());
        verify(socket, times(1)).setReuseAddress(true);
        verify(socket, times(1)).setKeepAlive(true);
        verify(socket, times(1)).setTcpNoDelay(true);
    }
    
    @Test
    public void testSocketSet() throws Exception {
        Socket socket = mock(Socket.class);
        TcpSocketOptions so = new TcpSocketOptions();
        so.setKeepAlive(false);
        so.setReceiveBufferSize(1);
        so.setSendBufferSize(2);
        so.setReuseAddress(false);
        so.setTcpNoDelay(false);
        so.setTimeout(3);
        
        so.applySocketOptions("testId", socket);
        verify(socket, times(1)).setReceiveBufferSize(1);
        verify(socket, times(1)).setSendBufferSize(2);
        verify(socket, times(1)).setSoTimeout(3);
        verify(socket, times(1)).setReuseAddress(false);
        verify(socket, times(1)).setKeepAlive(false);
        verify(socket, times(1)).setTcpNoDelay(false);
    }
    
    @Test
    public void testServerSocketNotSet() throws Exception {
        ServerSocket socket = mock(ServerSocket.class);
        TcpSocketOptions so = new TcpSocketOptions();
        so.clearTimeout();
        so.applySocketOptions("testId", socket);
        verify(socket, never()).setReceiveBufferSize(anyInt());
        verify(socket, never()).setSoTimeout(anyInt());
        verify(socket, times(1)).setReuseAddress(true);
    }
    
    @Test
    public void testServerSocketSet() throws Exception {
        ServerSocket socket = mock(ServerSocket.class);
        TcpSocketOptions so = new TcpSocketOptions();
        so.setReceiveBufferSize(1);
        so.setReuseAddress(false);
        so.setTimeout(3);
        
        so.applySocketOptions("testId", socket);
        verify(socket, times(1)).setReceiveBufferSize(1);
        verify(socket, times(1)).setSoTimeout(3);
        verify(socket, times(1)).setReuseAddress(false);
    }
    
    @Test(expected = ChannelException.class)
    public void testSocketThrowsException() throws Exception {
        Socket socket = mock(Socket.class);
        try {
            doAnswer(new TestAnswer(new SocketException())).when(socket).setReceiveBufferSize(anyInt());
        } catch(SocketException e) {
            // expected
        }
        
        TcpSocketOptions so = new TcpSocketOptions();
        so.setReceiveBufferSize(1);
        so.applySocketOptions("testId", socket);
    }
    
    @Test(expected = ChannelException.class)
    public void testServerSocketThrowsException() throws Exception {
        ServerSocket socket = mock(ServerSocket.class);
        try {
            doAnswer(new TestAnswer(new SocketException())).when(socket).setReceiveBufferSize(anyInt());
        } catch(SocketException e) {
            // expected
        }
        
        TcpSocketOptions so = new TcpSocketOptions();
        so.setReceiveBufferSize(1);
        so.applySocketOptions("testId", socket);
    }
    
    @Test
    public void testGettersSetters() {
        TcpSocketOptions so = new TcpSocketOptions();
        so.setKeepAlive(false);
        so.setReceiveBufferSize(1);
        so.setReuseAddress(false);
        so.setSendBufferSize(2);
        so.setTcpNoDelay(false);
        so.setTimeout(3);
        
        assertFalse(so.getKeepAlive());
        assertEquals(1, so.getReceiveBufferSize().getAsInt());
        assertFalse(so.isReuseAddress());
        assertEquals(2, so.getSendBufferSize().getAsInt());
        assertFalse(so.getTcpNoDelay());
        assertEquals(3, so.getTimeout().getAsInt());
    }
    
    @Test
    public void testInfoNotEnabled() throws Exception {
        Logger logger = (Logger)LogManager.getLogger(TcpSocketOptions.class);
        logger.setLevel(Level.OFF);
        
        TcpSocketOptions so = new TcpSocketOptions();
        Socket socket = mock(Socket.class);
        so.applySocketOptions("testId", socket);
        
        ServerSocket serverSocket = mock(ServerSocket.class);
        so.applySocketOptions("testId", serverSocket);
    }
}
