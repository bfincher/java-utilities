package com.fincher.iochannel.tcp;

import com.fincher.iochannel.SocketIoChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TcpChannelBuilder<CHANNEL extends TcpChannel, B extends TcpChannelBuilder<CHANNEL, B>>
        extends SocketIoChannelBuilder<CHANNEL, B> {

    private Optional<TcpSocketOptions> socketOptions = Optional.empty();
    private Optional<StreamIo> streamIo = Optional.empty();
    private final List<ConnectionEstablishedListener> connectionListeners = new ArrayList<>();

    public Optional<TcpSocketOptions> getSocketOptions() {
        return socketOptions;
    }

    public B withSocketOptions(TcpSocketOptions socketOptions) {
        this.socketOptions = Optional.of(socketOptions);
        return self();
    }

    public Optional<StreamIo> getStreamIo() {
        return streamIo;
    }

    public B withStreamIo(StreamIo streamIo) {
        this.streamIo = Optional.of(streamIo);
        return self();
    }

    public List<ConnectionEstablishedListener> getConnectionEstablishedListeners() {
        return connectionListeners;
    }

    public B addConnectionEstablishedListener(ConnectionEstablishedListener listener) {
        connectionListeners.add(listener);
        return self();
    }

    @Override
    public CHANNEL build() {
        CHANNEL channel = super.build();

        socketOptions.ifPresent(channel::setSocketOptions);
        connectionListeners.forEach(channel::addConnectionEstablishedListener);
        return channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected B self() {
        return (B) this;
    }

}
