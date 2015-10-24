/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package dk.ilios.spanner.options;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import java.io.File;

import dk.ilios.spanner.util.ShortDuration;
import dk.ilios.spanner.config.SpannerConfiguration;

/**
 * Runtime options for all tests. These will take precedence if conflicting with
 * {@link SpannerConfiguration}.
 */
public interface SpannerOptions {
    String benchmarkClassName();

    ImmutableSet<String> benchmarkMethodNames();

    ImmutableSet<String> vmNames();

    ImmutableSetMultimap<String, String> userParameters();

    ImmutableSetMultimap<String, String> vmArguments();

    ImmutableMap<String, String> configProperties();

    ImmutableSet<String> instrumentNames();

    int trialsPerScenario();

    ShortDuration timeLimit();

    String runName();

    boolean printConfiguration();

    boolean dryRun();

    File spannerDirectory();

    File spannerConfigFile();
}