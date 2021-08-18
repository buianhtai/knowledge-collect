 private ApiException noFallBackHandler(NoFallbackAvailableException ex) {
        return Optional.of(ex)
                .map(Throwable::getCause)
                .map(cause -> {
                    if (cause instanceof BaseApiException) {
                        throw (ApiException) cause;
                    }
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    } else if (cause instanceof Error) {
                        throw (Error) cause;
                    } else {
                        return new ApiException(cause.getMessage(), cause);
                    }
                }).orElseThrow(ApiException::new);
    }
