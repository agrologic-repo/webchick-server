package com.agrologic.app.network.rxtx;

public interface Network {

    public void setThreadState(NetworkState networkState);

    public NetworkState getNetworkState();
}
