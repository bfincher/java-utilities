package com.fincher.iochannel;

import com.fincher.iochannel.tcp.TcpClientChannelBuilder;
import com.fincher.iochannel.tcp.TcpServerChannelBuilder;
import com.fincher.iochannel.udp.UdpChannelBuilder;
import com.fincher.iochannel.udp.UdpMulticastChannelBuilder;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A utility to build and configure IOChannels.
 * 
 * @author bfincher
 *
 * @param <T> The type of data exchanged with the IO Channel
 */
public abstract class IoChannelBuilder<T extends Exchangeable, Channel extends IoChannelIfc<T>,
        B extends IoChannelBuilder<T, Channel, B>> {

    private String id;
    private IoType ioType;
    private List<Consumer<T>> messageListeners = new ArrayList<>();
    private boolean hasBeenBuilt = false;

    public static TcpServerChannelBuilder createForTcpServer() {
        return new TcpServerChannelBuilder();
    }

    public static TcpClientChannelBuilder createForTcpClient() {
        return new TcpClientChannelBuilder();
    }

    public UdpChannelBuilder createForUnicast() {
        return new UdpChannelBuilder();
    }

    public static UdpMulticastChannelBuilder createForMulticast() {
        return new UdpMulticastChannelBuilder();
    }

    public String getId() {
        return id;
    }

    /** Specifies the ID of this channel.
     * 
     * @param id The ID
     * @return This builder
     */
    public B withId(String id) {
        Preconditions.checkNotNull(id);
        this.id = id;
        return self();
    }

    public IoType getIoType() {
        return ioType;
    }

    /** Specifies the IO type of this channel.
     * If OUTPUT_ONLY, no message listeners may be set
     * @param ioType The IO Type of this channel 
     * @return This builder
     */
    public B withIoType(IoType ioType) {
        Preconditions.checkNotNull(ioType);
        Preconditions.checkState(messageListeners.isEmpty() || this.ioType.isInput(),
                "Message listeners cannot be added for output only channels");

        this.ioType = ioType;
        return self();
    }

    public List<Consumer<T>> getMessageListeners() {
        return messageListeners;
    }

    /** Adds a message listener to this channel.
     * Cannot be set on OUTPUT_ONLY CHANNELS
     * 
     * @param messageListener The message listener to add to the channel
     * @return This builder
     */
    public B withMessageListener(Consumer<T> messageListener) {
        Preconditions.checkState(ioType == null || ioType.isInput(),
                "Message listeners cannot be added for output only channels");
        Preconditions.checkNotNull(messageListener);
        messageListeners.add(messageListener);
        return self();
    }

    /** Builds a channel.
     * 
     * @return The built channel
     */
    public Channel build() {
        Preconditions.checkState(!hasBeenBuilt, "A builder can only build a channel once");
        Preconditions.checkState(id != null, "An ID must be set prior to building");
        Preconditions.checkState(ioType != null, "an IO Type must be set prior to building");
        hasBeenBuilt = true;
        return doBuild();
    }

    protected abstract Channel doBuild();

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

}
