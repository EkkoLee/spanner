/**
 * Copyright (C) 2010 Google Inc.
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

package com.google.caliper.cloud.client;

import com.google.gwt.core.client.GWT;

/**
 * A link to delete (and then undelete) a snapshot.
 */
public final class SnapshotDeletedEditor extends DeletedEditor<BenchmarkSnapshotMeta> {

  private BenchmarkServiceAsync benchmarkService = GWT.create(BenchmarkService.class);

  public SnapshotDeletedEditor(BenchmarkSnapshotMeta snapshot) {
    super(snapshot);
  }

  @Override
  protected void persistDeletion() {
    benchmarkService.setSnapshotDeleted(deletable.getId(), true, deletionCallback);
  }

  @Override
  protected void persistUndeletion() {
    benchmarkService.setSnapshotDeleted(deletable.getId(), false, undeletionCallback);
  }
}
