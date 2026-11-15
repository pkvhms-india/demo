package com.varicent.common.concurrency.dao;

public interface ConcurrencyDao {
	boolean isGlobalLocked();

	boolean BufferTableEmpty(String var1);
}
