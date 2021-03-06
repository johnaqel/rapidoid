/*-
 * #%L
 * rapidoid-commons
 * %%
 * Copyright (C) 2014 - 2018 Nikolche Mihajlovski and contributors
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

package org.rapidoid.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.collection.Coll;
import org.rapidoid.test.AbstractCommonsTest;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Authors("Nikolche Mihajlovski")
@Since("5.0.4")
public class AutoExpandingMapTest extends AbstractCommonsTest {

	@Test
	public void testConcurrentMapAccess() {
		final AtomicInteger counter = new AtomicInteger();
		final Map<Object, Object> map = autoToStr(counter);

		final int k = 1000;

		Assertions.assertTimeout(Duration.ofSeconds(60), () -> {
			Msc.benchmarkMT(200, "gets", 100000000, () -> {
				int rnd = Rnd.rnd(k);
				eq(map.get(rnd), rnd + "");
			});
		});

		// it is highly unlikely to be less than K, for a small value of K
		eq(counter.get(), k);
	}

	@Test
	public void testEquals() {
		final AtomicInteger counter = new AtomicInteger();
		final Map<Object, Object> map = autoToStr(counter);

		map.get("a");
		map.get(1);
		map.get(true);

		eq(map, U.map("a", "a", 1, "1", true, "true"));
	}

	private Map<Object, Object> autoToStr(final AtomicInteger counter) {
		return Coll.autoExpandingMap(src -> {
			counter.incrementAndGet();
			return src + "";
		});
	}
}
