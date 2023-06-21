/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.common.sdk.service;

import io.github.pnoker.common.dto.DriverSyncDownDTO;

/**
 * 驱动同步相关接口
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverSyncService {

    /**
     * 同步驱动信息到平台端
     */
    void up();

    /**
     * 同步平台端信息到驱动
     *
     * @param entityDTO DriverSyncDTO
     */
    void down(DriverSyncDownDTO entityDTO);
}
