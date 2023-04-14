/**
 * Licensed to LinDB under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. LinDB licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.lindb.client.api;

import java.util.List;

/**
 * Error event listener when write data failure.
 */
public interface EventListener {

	/**
	 * onEvent will be called when write data failure.
	 *
	 * @param event  event type
	 * @param points points of failed
	 * @param e      exception
	 */
	void onError(EventType event, List<Point> points, final Throwable e);
}
