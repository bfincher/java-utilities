package com.fincher.iochannel.udp;

import com.fincher.iochannel.IoType;
import com.fincher.iochannel.SocketIoChannelBuilder;
import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;
import java.util.Optional;

public class UdpChannelBuilder
        extends
        SocketIoChannelBuilder<UdpChannel, UdpChannelBuilder> {

    private Optional<InetSocketAddress> remoteAddress = Optional.empty();
    private Optional<UdpSocketOptions> socketOptions;

    public Optional<InetSocketAddress> getRemoteAddress() {
        return remoteAddress;
    }

    /** Sets the destination address for the channel.
     * 
     * @param remoteAddress The destination address for this channel.
     * @return This builder
     */
    public UdpChannelBuilder sendingToRemoteAddress(InetSocketAddress remoteAddress) {
        Preconditions.checkState(getIoType() == null || getIoType().isOutput(),
                "Remote address cannot be set on an input only channel");
        this.remoteAddress = Optional.of(remoteAddress);
        return this;
    }

    @Override
    public UdpChannelBuilder withIoType(IoType ioType) {
        Preconditions.checkState(remoteAddress.isEmpty() || ioType.isOutput(),
                "Remote address cannot be set on an input only channel");
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
        Preconditions.checkState(getIoType().isInput() || remoteAddress.isPresent(),
                "A remote address must be set for output channels");
        return UdpChannel.create(this);
    }

}
