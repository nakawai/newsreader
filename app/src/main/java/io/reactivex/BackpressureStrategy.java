package io.reactivex;

/**
 * Dummy class
 * https://stackoverflow.com/questions/47061821/realm-noclassdeffounderror-rx-observable/47062181#47062181
 */
@SuppressWarnings("unused")
public enum BackpressureStrategy {
    MISSING,
    ERROR,
    BUFFER,
    DROP,
    LATEST
}
