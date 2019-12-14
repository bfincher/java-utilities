package com.fincher.iochannel.tcp;

import com.google.common.base.Preconditions;

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
        Preconditions.checkState(remoteAddress.isPresent(), "Remote address must be set prior to building");
        return new TcpClientChannel(this);
    }

}
