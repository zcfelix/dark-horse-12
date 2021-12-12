package com.thoughtworks.darkhorse.reservationsystem.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageResponse<T> {
    List<T> contents;
    Long totalSize;
}
