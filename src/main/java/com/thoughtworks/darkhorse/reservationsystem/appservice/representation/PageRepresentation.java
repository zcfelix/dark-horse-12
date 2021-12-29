package com.thoughtworks.darkhorse.reservationsystem.appservice.representation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageRepresentation<T> {
    List<T> contents;
    Long totalSize;
}
