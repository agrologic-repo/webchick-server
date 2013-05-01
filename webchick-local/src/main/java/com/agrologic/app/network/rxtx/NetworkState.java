package com.agrologic.app.network.rxtx;

public enum NetworkState {
    STATE_IDLE, STATE_STARTING, STATE_SEND, STATE_READ, STATE_BUSY, STATE_TIMEOUT, STATE_ERROR,
    STATE_DELAY, STATE_STOP, STATE_ABORT,
}
