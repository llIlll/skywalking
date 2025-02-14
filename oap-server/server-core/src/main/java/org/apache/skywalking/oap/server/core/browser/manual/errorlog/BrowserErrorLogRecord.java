/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.skywalking.oap.server.core.browser.manual.errorlog;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.analysis.Stream;
import org.apache.skywalking.oap.server.core.analysis.record.Record;
import org.apache.skywalking.oap.server.core.analysis.worker.RecordStreamProcessor;
import org.apache.skywalking.oap.server.core.source.DefaultScopeDefine;
import org.apache.skywalking.oap.server.core.storage.annotation.BanyanDBShardingKey;
import org.apache.skywalking.oap.server.core.storage.annotation.Column;
import org.apache.skywalking.oap.server.core.storage.annotation.SuperDataset;
import org.apache.skywalking.oap.server.core.storage.type.Convert2Entity;
import org.apache.skywalking.oap.server.core.storage.type.Convert2Storage;
import org.apache.skywalking.oap.server.core.storage.type.StorageBuilder;

@SuperDataset
@Stream(name = BrowserErrorLogRecord.INDEX_NAME, scopeId = DefaultScopeDefine.BROWSER_ERROR_LOG, builder = BrowserErrorLogRecord.Builder.class, processor = RecordStreamProcessor.class)
public class BrowserErrorLogRecord extends Record {
    public static final String INDEX_NAME = "browser_error_log";
    public static final String UNIQUE_ID = "unique_id";
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_VERSION_ID = "service_version_id";
    public static final String PAGE_PATH_ID = "page_path_id";
    public static final String TIMESTAMP = "timestamp";
    public static final String ERROR_CATEGORY = "error_category";
    public static final String DATA_BINARY = "data_binary";

    @Override
    public String id() {
        return uniqueId;
    }

    @Setter
    @Getter
    @Column(columnName = UNIQUE_ID)
    private String uniqueId;

    @Setter
    @Getter
    @Column(columnName = SERVICE_ID)
    @BanyanDBShardingKey(index = 0)
    private String serviceId;

    @Setter
    @Getter
    @Column(columnName = SERVICE_VERSION_ID)
    private String serviceVersionId;

    @Setter
    @Getter
    @Column(columnName = PAGE_PATH_ID)
    private String pagePathId;

    @Setter
    @Getter
    @Column(columnName = TIMESTAMP)
    private long timestamp;

    @Setter
    @Getter
    @Column(columnName = ERROR_CATEGORY)
    private int errorCategory;

    @Setter
    @Getter
    @Column(columnName = DATA_BINARY)
    private byte[] dataBinary;

    public static class Builder implements StorageBuilder<BrowserErrorLogRecord> {
        @Override
        public BrowserErrorLogRecord storage2Entity(final Convert2Entity converter) {
            BrowserErrorLogRecord record = new BrowserErrorLogRecord();
            record.setUniqueId((String) converter.get(UNIQUE_ID));
            record.setServiceId((String) converter.get(SERVICE_ID));
            record.setServiceVersionId((String) converter.get(SERVICE_VERSION_ID));
            record.setPagePathId((String) converter.get(PAGE_PATH_ID));
            record.setTimestamp(((Number) converter.get(TIMESTAMP)).longValue());
            record.setTimeBucket(((Number) converter.get(TIME_BUCKET)).longValue());
            record.setErrorCategory(((Number) converter.get(ERROR_CATEGORY)).intValue());
            record.setDataBinary(converter.getBytes(DATA_BINARY));
            return record;
        }

        @Override
        public void entity2Storage(final BrowserErrorLogRecord storageData, final Convert2Storage converter) {
            converter.accept(UNIQUE_ID, storageData.getUniqueId());
            converter.accept(SERVICE_ID, storageData.getServiceId());
            converter.accept(SERVICE_VERSION_ID, storageData.getServiceVersionId());
            converter.accept(PAGE_PATH_ID, storageData.getPagePathId());
            converter.accept(TIMESTAMP, storageData.getTimestamp());
            converter.accept(TIME_BUCKET, storageData.getTimeBucket());
            converter.accept(ERROR_CATEGORY, storageData.getErrorCategory());
            converter.accept(DATA_BINARY, storageData.getDataBinary());
        }
    }
}
