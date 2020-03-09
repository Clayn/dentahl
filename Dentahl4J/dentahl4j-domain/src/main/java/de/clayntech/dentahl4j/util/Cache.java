/*
 * The MIT License
 *
 * Copyright 2020 Clayn <clayn_osmato@gmx.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.clayntech.dentahl4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
public class Cache
{
    private final Logger LOG= LoggerFactory.getLogger(Cache.class);
    private static final long DECAY = TimeUnit.SECONDS.toMillis(30);
    private final Map<CacheEntry, Object> cacheMap = new HashMap<>();
    private final Map<CacheEntry, Supplier<Object>> getterMap = new HashMap<>();

    {
        LOG.info("Creating a new cache");
    }
    public void registerSupplier(String key, Supplier<Object> function)
    {
        getterMap.put(new CacheEntry(key, -1), function);
    }

    public <T> T get(String key)
    {
        for (Map.Entry<CacheEntry, Object> entry : cacheMap.entrySet())
        {
            if (entry.getKey().getKey().equals(key))
            {
                long time = entry.getKey().getCreated();
                if (time + DECAY < System.currentTimeMillis())
                {
                    if (!getterMap.containsKey(entry.getKey()))
                    {
                        return null;
                    }
                    LOG.info("Recalculating the value for {}",key);
                    Object val = getterMap.get(entry.getKey()).get();
                    CacheEntry newEntry = new CacheEntry(key,
                            System.currentTimeMillis());
                    cacheMap.put(newEntry, val);
                    return (T) val;
                } else
                {
                    LOG.info("Retrieving cached value for {}",key);
                    return (T) entry.getValue();
                }
            }
        }
        CacheEntry keyEntry = new CacheEntry(key, DECAY);
        if (!getterMap.containsKey(keyEntry))
        {
            LOG.info("No information of values found for {}",key);
            return null;
        }
        LOG.info("Calculating the value for {}",key);
        Object val = getterMap.get(keyEntry).get();
        CacheEntry newEntry = new CacheEntry(key, System.currentTimeMillis());
        cacheMap.put(newEntry, val);
        return (T) val;
    }

    private static class CacheEntry
    {

        private final String key;
        private final long created;

        public CacheEntry(String key, long created)
        {
            this.key = key;
            this.created = created;
        }

        public long getCreated()
        {
            return created;
        }

        public String getKey()
        {
            return key;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.key);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            final CacheEntry other = (CacheEntry) obj;
            if (!Objects.equals(this.key, other.key))
            {
                return false;
            }
            return true;
        }

    }

}
