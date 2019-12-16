package com.fincher.iochannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SocketOptionsTest {
    
    @Test
    public void test() {
        SocketOptions so = new SocketOptions();
        assertTrue(so.getSendBufferSize().isEmpty());
        assertTrue(so.getReceiveBufferSize().isEmpty());
        assertTrue(so.isReuseAddress());
        assertEquals(2000, so.getTimeout().getAsInt());
        
        so.setSendBufferSize(1);
        so.setReceiveBufferSize(2);
        so.setReuseAddress(false);
        so.setTimeout(4);
        
        assertEquals(1, so.getSendBufferSize().getAsInt());
        assertEquals(2, so.getReceiveBufferSize().getAsInt());
        assertFalse(so.isReuseAddress());
        assertEquals(4, so.getTimeout().getAsInt());
        
        so.clearSendBufferSize();
        so.clearReceiveBufferSize();
        so.clearTimeout();
        assertTrue(so.getSendBufferSize().isEmpty());
        assertTrue(so.getReceiveBufferSize().isEmpty());
        assertTrue(so.getTimeout().isEmpty());
    }

}
