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

package net.cpollet.jixture.support.spring;

import net.cpollet.jixture.support.spring.annotation.FixtureDef;
import net.cpollet.jixture.support.spring.annotation.Jixture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public class JixtureTestExecutionListenerSetupHandler extends BaseJixtureTestExecutionListenerWriteHandler {
	@Override
	protected List<FixtureDef> parseJixtureAnnotation(Jixture jixtureAnnotation) {
		if (null != jixtureAnnotation) {
			return Arrays.asList(jixtureAnnotation.setup());
		}

		return Collections.emptyList();
	}
}
