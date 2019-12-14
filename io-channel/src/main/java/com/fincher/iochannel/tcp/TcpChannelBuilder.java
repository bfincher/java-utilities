package com.fincher.iochannel.tcp;

import com.fincher.iochannel.SocketIoChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class TcpChannelBuilder<Channel extends TcpChannel, B extends TcpChannelBuilder<Channel, B>>
        extends
        SocketIoChannelBuilder<Channel, TcpChannelBuilder<Channel, B>> {

    private Optional<TcpSocketOptions> socketOptions = Optional.empty();
    private Optional<StreamIo> streamIo = Optional.empty();
    private final List<ConnectionEstablishedListener> connectionListeners = new ArrayList<>();

    public Optional<TcpSocketOptions> getSocketOptions() {
        return socketOptions;
    }

    public B setSocketOptions(TcpSocketOptions socketOptions) {
        this.socketOptions = Optional.of(socketOptions);
        return self();
    }

    public Optional<StreamIo> getStreamIo() {
        return streamIo;
    }

    public B setStreamIo(StreamIo streamIo) {
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
    public Channel build() {
        Channel channel = super.build();

        socketOptions.ifPresent(options -> channel.setSocketOptions(options));
        connectionListeners.forEach(listener -> channel.addConnectionEstablishedListener(listener));
        return channel;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected B self() {
        return (B) this;
    }

}
