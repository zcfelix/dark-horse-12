package com.thoughtworks.darkhorse.reservationsystem.domainservice.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<T> {
    private final List<T> elements;
    private final Long totalSize;
}
