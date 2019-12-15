package com.fincher.iochannel.tcp;

import java.net.InetSocketAddress;

public class TcpServerChannelBuilder extends TcpChannelBuilder<TcpServerChannel, TcpServerChannelBuilder> {

    @Override
    protected TcpServerChannel doBuild() {
        TcpServerChannel channel = new TcpServerChannel(getId(), getIoType(),
                getStreamIo().orElseThrow(() -> new IllegalStateException("Stream IO must be set")),
                localAddress.orElse(new InetSocketAddress(0)));

        return channel;
    }

}
