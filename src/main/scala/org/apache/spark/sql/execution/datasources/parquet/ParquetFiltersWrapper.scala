/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.execution.datasources.parquet

import org.apache.parquet.filter2.predicate.FilterPredicate

import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.sources
import org.apache.spark.sql.types.StructType

/**
 *  Wrap ParquetFilters so it can be accessed outside of the parquet package.
 */
object ParquetFiltersWrapper {
  def createFilter(
      conf: SQLConf, schema: StructType,
      predicate: sources.Filter): Option[FilterPredicate] = {
    val parquetFilters =
      new ParquetFilters(conf.parquetFilterPushDownDate, conf.parquetFilterPushDownTimestamp,
      conf.parquetFilterPushDownDecimal, conf.parquetFilterPushDownStringStartWith,
      conf.parquetFilterPushDownInFilterThreshold, conf.caseSensitiveAnalysis)
    parquetFilters.createFilter(new SparkToParquetSchemaConverter(conf).convert(schema), predicate)
  }
}
