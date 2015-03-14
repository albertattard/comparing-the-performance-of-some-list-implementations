/*
 * #%L
 * Comparing the Performance of some List Implementations
 * $Id:$
 * $HeadURL: https://java-creed-examples.googlecode.com/svn/collections/Comparing%20the%20Performance%20of%20some%20List%20Implementations/src/main/java/com/javacreed/examples/collections/list/TimeListIteratorAction.java $
 * %%
 * Copyright (C) 2012 - 2014 Java Creed
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.javacreed.examples.collections.list;

import java.util.Iterator;
import java.util.List;

public class TimeListIteratorAction implements TimeListAction {

  private final String pattern = "Element %d";

  @Override
  public String getName() {
    return "iterate()";
  }

  @Override
  public long timeAction(final List<String> list, final int limit) {
    for (int i = 0; i < limit; i++) {
      list.add(String.format(pattern, i));
    }

    final long start = System.nanoTime();
    for (final Iterator<String> i = list.iterator(); i.hasNext();) {
      i.next();
    }
    return System.nanoTime() - start;
  }

}
