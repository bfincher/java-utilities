package com.fincher.iochannel.tcp;

public class TcpServerChannelBuilder extends TcpChannelBuilder<TcpServerChannel, TcpServerChannelBuilder> {
    
    @Override
    protected TcpServerChannel doBuild() {
        return new TcpServerChannel(this);
    }

}
