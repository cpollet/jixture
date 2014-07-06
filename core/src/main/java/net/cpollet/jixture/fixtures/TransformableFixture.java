/*
 * Copyright 2013 Christophe Pollet
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

/**
 * A fixture that is not loadable into database without being transformed to another kind of fixture. This interface is
 * mainly a maker for the {@link net.cpollet.jixture.fixtures.loaders.FixtureLoader} and the transformer method is
 * defined in {@link net.cpollet.jixture.fixtures.transformers.FixtureTransformer}.
 *
 * @see net.cpollet.jixture.fixtures.transformers.FixtureTransformer
 *
 * @author Christophe Pollet
 */
public interface TransformableFixture extends Fixture {
}
