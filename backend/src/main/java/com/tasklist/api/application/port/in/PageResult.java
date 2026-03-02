package com.tasklist.api.application.port.in;

import java.util.List;

public record PageResult<T>(List<T> content, int page, int size) {
}
