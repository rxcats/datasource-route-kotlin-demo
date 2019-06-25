package io.github.rxcats.datasourceroute

import java.lang.RuntimeException

class InvalidShardNoException(message: String) : RuntimeException(message)

class NotFoundUserException(message: String) : RuntimeException(message)