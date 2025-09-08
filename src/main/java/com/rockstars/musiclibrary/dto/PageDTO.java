package com.rockstars.musiclibrary.dto;

import java.io.Serializable;
import java.util.List;

public record PageDTO<T>(List<T> content, long totalElements, int pageNumber, int pageSize) implements Serializable {}