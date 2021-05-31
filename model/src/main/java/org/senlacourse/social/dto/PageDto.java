package org.senlacourse.social.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDto<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
}
