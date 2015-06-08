package org.rapidoid.entity;

/*
 * #%L
 * rapidoid-entity
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
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

/**
 * @author Nikolche Mihajlovski
 * @since 2.2.0
 */
public abstract class AbstractEntity implements IEntity {

	private static final long serialVersionUID = 6714174690388273909L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id() == null) ? 0 : id().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		IEntity other = (IEntity) obj;

		if (id() == null || other.id() == null) {
			return this == obj;
		}

		return id().equals(other.id());
	}

	@Override
	public String toString() {
		return super.toString() + "[id=" + id() + ", version=" + version() + "]";
	}

}
