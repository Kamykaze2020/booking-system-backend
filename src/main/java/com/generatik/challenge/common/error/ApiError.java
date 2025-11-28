package com.generatik.challenge.common.error;

import java.util.Map;

public record ApiError(String message, Map<String, String> fieldErrors) {}

