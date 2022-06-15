/*
 * MIT License
 *
 * Copyright (c) 2022 Kevin Lin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.*;

/**
 * A recursive enumeration problem that can print all successful combinations of options with the
 * given element type. Options are defined by a method that returns an iterable over the given
 * element type. A combination of options is defined as a successful combination or a partial
 * combination via their respective methods.
 *
 * @author Kevin Lin
 */
public interface Problem<E> {

    /**
     * Returns true if the given list is a valid, successful combination.
     *
     * @param soFar the combination of options
     * @return true if the combination is valid and successful (complete)
     */
    public boolean isSuccess(List<E> soFar);

    /**
     * Returns true if the given list is a valid, partial combination.
     *
     * @param soFar the combination of options
     * @return true if the combination is valid and partial (incomplete)
     */
    public boolean isPartial(List<E> soFar);

    /**
     * Returns all the options that can be explored.
     *
     * @return an iterable containing all options
     */
    public Iterable<E> options();

    /**
     * Prints-out all the valid, successful combinations of options.
     */
    public default void enumerate() {
        enumerate(new ArrayList<E>());
    }

    private void enumerate(List<E> soFar) {
        if (isSuccess(soFar)) {
            System.out.println(soFar);
        } else if (isPartial(soFar)) {
            for (E option : options()) {
                soFar.add(option);
                enumerate(soFar);
                soFar.remove(soFar.size() - 1);
            }
        }
    }
}
