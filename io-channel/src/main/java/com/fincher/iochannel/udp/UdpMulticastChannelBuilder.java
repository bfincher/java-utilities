package com.fincher.iochannel.udp;

import com.fincher.iochannel.IoType;
import com.fincher.iochannel.SocketIoChannelBuilder;
import com.google.common.base.Preconditions;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

public class UdpMulticastChannelBuilder
        extends SocketIoChannelBuilder<UdpMulticastChannel, UdpMulticastChannelBuilder> {

    private Optional<InetSocketAddress> remoteAddress = Optional.empty();
    private Optional<InetAddress> multicastAddress = Optional.empty();
    private Optional<UdpMulticastSocketOptions> socketOptions = Optional.empty();

    public Optional<InetSocketAddress> getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Set the remote address that this channel will send to. Only applicable for output channels
     * 
     * @param remoteAddress The destination multicast address and remote port
     * @return This builder
     */
    public UdpMulticastChannelBuilder sendingToRemoteAddress(InetSocketAddress remoteAddress) {
        Preconditions.checkNotNull(remoteAddress);
        Preconditions.checkArgument(remoteAddress.getAddress().isMulticastAddress(),
                "The remote address must be multicast");
        this.remoteAddress = Optional.of(remoteAddress);
        return this;
    }

    public Optional<InetAddress> getMulticastAddress() {
        return multicastAddress;
    }

    /**
     * Specifies the multicast address this channel will receive on. Only applicable for input channels
     * 
     * @param address The multicast address
     * @return This builder
     */
    public UdpMulticastChannelBuilder receivingFromMulticastAddress(InetAddress address) {
        Preconditions.checkArgument(address.isMulticastAddress(), "Expected a multicast address");
        multicastAddress = Optional.of(address);
        return this;
    }

    @Override
    public UdpMulticastChannelBuilder withIoType(IoType ioType) {
        Preconditions.checkArgument(ioType != IoType.INPUT_AND_OUTPUT,
                "UDP Channels cannot have an IoType of INPUT_AND_OUTPUT");

        return super.withIoType(ioType);
    }

    public Optional<UdpMulticastSocketOptions> getSocketOptions() {
        return socketOptions;
    }

    public UdpMulticastChannelBuilder withSocketOptions(UdpMulticastSocketOptions socketOptions) {
        this.socketOptions = Optional.of(socketOptions);
        return this;
    }

    @Override
    protected UdpMulticastChannel doBuild() {
        UdpMulticastChannel channel;
        if (getIoType().isInput()) {
            Preconditions.checkState(remoteAddress.isEmpty(), "Remote address cannot be set for input channels");
            channel = new UdpMulticastChannel(getId(), 
                    getLocalAddress().orElse(new InetSocketAddress(0)),
                    getMulticastAddress().orElseThrow(() -> new IllegalStateException(
                            "Receiving from multicast address must be set for input channels")));
        } else {
            Preconditions.checkState(multicastAddress.isEmpty(),
                    "Receiving from multicast address cannot be set for output channels");
            channel = new UdpMulticastChannel(getId(), 
                    getLocalAddress().orElse(new InetSocketAddress(0)),
                    getRemoteAddress().orElseThrow(() -> new IllegalStateException(
                            "Remote address must be set for output channels")));
        }
        
        socketOptions.ifPresent(channel::setSocketOptions);
        
        return channel;
    }

}
