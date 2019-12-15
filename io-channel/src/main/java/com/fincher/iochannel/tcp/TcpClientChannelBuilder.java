package com.fincher.iochannel.tcp;

import java.net.InetSocketAddress;
import java.util.Optional;

public class TcpClientChannelBuilder extends TcpChannelBuilder<TcpClientChannel, TcpClientChannelBuilder> {
    
    private Optional<InetSocketAddress> remoteAddress = Optional.empty();
    
    public Optional<InetSocketAddress> getRemoteAddress() {
        return remoteAddress;
    }

    public TcpClientChannelBuilder withRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = Optional.of(remoteAddress);
        return self();
    }
    
    @Override
    protected TcpClientChannel doBuild() {
        
        TcpClientChannel channel = new TcpClientChannel(getId(),
                getIoType(),
                getStreamIo().orElseThrow(() -> new IllegalStateException("Stream IO must be set")),
                getLocalAddress().orElse(new InetSocketAddress(0)),
                getRemoteAddress().orElseThrow(() -> new IllegalStateException("remoteAddress must be set")));
        
        return channel;
    }

}
