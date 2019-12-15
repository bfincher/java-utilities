package com.fincher.iochannel.udp;

import com.fincher.iochannel.IoType;
import com.fincher.iochannel.SocketIoChannelBuilder;
import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;
import java.util.Optional;

public class UdpChannelBuilder extends SocketIoChannelBuilder<UdpChannel, UdpChannelBuilder> {

    private Optional<InetSocketAddress> remoteAddress = Optional.empty();
    private Optional<UdpSocketOptions> socketOptions = Optional.empty();

    public Optional<InetSocketAddress> getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Sets the destination address for the channel.
     * 
     * @param remoteAddress The destination address for this channel.
     * @return This builder
     */
    public UdpChannelBuilder sendingToRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = Optional.of(remoteAddress);
        return this;
    }

    @Override
    public UdpChannelBuilder withIoType(IoType ioType) {
        Preconditions.checkArgument(ioType != IoType.INPUT_AND_OUTPUT,
                "UDP Channels cannot have an IoType of INPUT_AND_OUTPUT");
        return super.withIoType(ioType);
    }

    public Optional<UdpSocketOptions> getSocketOptions() {
        return socketOptions;
    }

    public UdpChannelBuilder withSocketOptions(UdpSocketOptions socketOptions) {
        this.socketOptions = Optional.of(socketOptions);
        return this;
    }

    @Override
    protected UdpChannel doBuild() {
        UdpChannel channel;
        if (getIoType().isInput()) {
            Preconditions.checkState(remoteAddress.isEmpty(), "Remote address cannot be set on input channels");
            channel = new UdpChannel(getId(), getIoType(), getLocalAddress().orElse(new InetSocketAddress(0)));
        } else {
            channel = new UdpChannel(getId(), getIoType(), getLocalAddress().orElse(new InetSocketAddress(0)),
                    getRemoteAddress().orElseThrow(() -> new IllegalStateException("Remote Address must be set")));
        }

        getSocketOptions().ifPresent(channel::setSocketOptions);
        return channel;
    }

}
