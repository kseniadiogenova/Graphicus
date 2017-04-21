package com.gui.graphicus;

import java.util.Map;

/**
 *
 * @param <T>
 */

public interface FunctionInterface<T> {
    public double getResult(T arg1, T arg2, Map<String, Double> vars);
}
