package com.mshmidov.chains.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class StringIterator<T> implements Iterator<T> {

    private final String string;

    private final Function<Character, T> mapper;

    private int position = 0;

    protected StringIterator(String string, Function<Character, T> mapper) {
        this.string = string;
        this.mapper = mapper;
    }


    @Override
    public boolean hasNext() {
        return position < string.length();
    }


    @Override
    public T next() {
        try {
            return mapper.apply(string.charAt(position++));
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

}
