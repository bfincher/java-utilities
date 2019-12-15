package com.fincher.iochannel;

import java.net.InetSocketAddress;
import java.util.Optional;

public abstract class SocketIoChannelBuilder<CHANNEL extends SocketIoChannel,
        B extends SocketIoChannelBuilder<CHANNEL, B>>
        extends
        IoChannelBuilder<MessageBuffer, CHANNEL, B> {

    protected Optional<InetSocketAddress> localAddress = Optional.empty();

    public Optional<InetSocketAddress> getLocalAddress() {
        return localAddress;
    }

    public B atLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = Optional.of(localAddress);
        return self();
    }

}
