package com.fincher.iochannel;

import java.net.InetSocketAddress;
import java.util.Optional;

public abstract class SocketIoChannelBuilder<Channel extends SocketIoChannel,
        B extends SocketIoChannelBuilder<Channel, B>>
        extends
        IoChannelBuilder<MessageBuffer, Channel, B> {

    protected Optional<InetSocketAddress> localAddress = Optional.empty();

    public Optional<InetSocketAddress> getLocalAddress() {
        return localAddress;
    }

    public B atLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = Optional.of(localAddress);
        return self();
    }

}
