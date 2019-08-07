package com.shlee.githubsearch.data.executor

import java.util.concurrent.Executors

val DISK_IO = Executors.newSingleThreadExecutor()

val NETWORK_IO = Executors.newFixedThreadPool(5)
