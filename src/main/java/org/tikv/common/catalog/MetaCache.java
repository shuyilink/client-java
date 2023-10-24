package org.tikv.common.catalog;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import org.tikv.common.meta.TiDBInfo;
import org.tikv.common.meta.TiTableInfo;

class MetaCache {
  private static ReentrantLock dbMutex = new ReentrantLock();
  private static ReentrantLock tableMutex = new ReentrantLock();

  private static Map<String, TiDBInfo> dbCache;

  private static Map<TiDBInfo, Map<String, TiTableInfo>> tableCache;

  private static long lastDBInitTimestampMills;
  private static long lastTableInitTimestampMills;
  // db cache expired time
  private static long expiredDBMills = 2 * 60 * 1000;

  private static long expiredTableMills = 5 * 60 * 1000;

  public static Map<String, TiDBInfo> getDBCache() {
    if (lastDBInitTimestampMills == 0) {
      return null;
    }
    long currTimestampMills = System.currentTimeMillis();
    if (currTimestampMills - lastDBInitTimestampMills > expiredDBMills) {
      return null;
    }
    return dbCache;
  }

  public static void setDBCache(Map<String, TiDBInfo> db) {
    dbCache = db;
    lastDBInitTimestampMills = System.currentTimeMillis();
  }

  public static Map<TiDBInfo, Map<String, TiTableInfo>> getTableCache() {
    if (lastTableInitTimestampMills == 0) {
      return null;
    }
    long currTimestampMills = System.currentTimeMillis();
    if (currTimestampMills - lastTableInitTimestampMills > expiredTableMills) {
      return null;
    }
    return tableCache;
  }

  public static void setTableCache(Map<TiDBInfo, Map<String, TiTableInfo>> table) {
    tableCache = table;
    lastTableInitTimestampMills = System.currentTimeMillis();
    return;
  }

  public static ReentrantLock getTableMutex() {
    return tableMutex;
  }

  public static ReentrantLock getDBMutex() {
    return dbMutex;
  }
}
