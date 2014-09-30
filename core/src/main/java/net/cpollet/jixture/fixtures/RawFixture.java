/*
 * Copyright 2014 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cpollet.jixture.fixtures;


import net.cpollet.jixture.dao.UnitDaoFactory;

/**
 * A fixture that is loaded via a file containing raw database statements.
 *
 * @author Christophe Pollet
 */
public interface RawFixture extends Fixture {
	/**
	 * Execute the raw file's content.
	 *
	 * @param unitDaoFactory the {@link net.cpollet.jixture.dao.UnitDaoFactory} to use.
	 */
	public void load(UnitDaoFactory unitDaoFactory);
}
