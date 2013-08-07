package com.riotapps.word.utils;

public class Cache extends LruCache<String, Object>{
 
  public Cache(int maxSizeInBytes) {
 
   super(maxSizeInBytes);
 
  }
 
 //we can add overrides here later as needed
 }

