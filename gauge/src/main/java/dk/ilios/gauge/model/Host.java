/*
 * Copyright (C) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.ilios.gauge.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static dk.ilios.gauge.util.PersistentHashing.getPersistentHashFunction;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashFunction;
import com.google.common.hash.PrimitiveSink;

import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Logger;

import dk.ilios.gauge.util.StringMapFunnel;

/**
 * The performance-informing properties of the host on which a benchmark is run.
 *
 * @author gak@google.com (Gregory Kick)
 */
public final class Host {
    static final Host DEFAULT = new Host();
    private static final Logger logger = Logger.getLogger(Host.class.getName());

    private int id;
    private SortedMap<String, String> properties;
    private int hash;

    private Host() {
        this.properties = Maps.newTreeMap();
    }

    private Host(Builder builder) {
        this.properties = Maps.newTreeMap(builder.properties);
        // eagerly initialize hash to allow for the test-only hash function
        initHash(builder.hashFunction);
    }

    public ImmutableSortedMap<String, String> properties() {
        return ImmutableSortedMap.copyOf(properties);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Host) {
            Host that = (Host) obj;
            return this.properties.equals(that.properties);
        } else {
            return false;
        }
    }

    private void initHash(HashFunction hashFunction) {
        if (hash == 0) {
            this.hash = hashFunction.hashObject(this, HostFunnel.INSTANCE).asInt();
        }
    }

    private void initHash() {
        initHash(getPersistentHashFunction());
    }

    @Override
    public int hashCode() {
        initHash();
        return hash;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("properties", properties)
                .toString();
    }

    public enum HostFunnel implements Funnel<Host> {
        INSTANCE;

        @Override
        public void funnel(Host from, PrimitiveSink into) {
            StringMapFunnel.INSTANCE.funnel(from.properties, into);
        }
    }

    public static final class Builder {
        private final SortedMap<String, String> properties = Maps.newTreeMap();
        private HashFunction hashFunction = getPersistentHashFunction();

        public Builder addProperty(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public Builder addAllProperies(Map<String, String> properties) {
            this.properties.putAll(properties);
            return this;
        }

        /**
         * This only exists for tests to induce hash collisions. Only use this in test code as changing
         * the hash function will break persisted objects.
         */
        @VisibleForTesting
        public Builder hashFunctionForTesting(HashFunction hashFunction) {
            logger.warning("somebody is setting the hash function. this should only be used in tests");
            this.hashFunction = checkNotNull(hashFunction);
            return this;
        }

        public Host build() {
            return new Host(this);
        }
    }
}
