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

package net.cpollet.jixture.support.hook;

import net.cpollet.jixture.fixtures.loaders.FixtureLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Christophe Pollet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface DataSource {
	FixtureLoader.Mode mode() default FixtureLoader.Mode.NO_COMMIT;

	String[] order() default {//
			"builders",//
			"cleaning",//
			"springContextPaths",//
			"sqlFilePaths",//
			"sqlQueries",//
			"xlsFilePaths",//
			"xlsxFilePaths",//
			"xmlFilePaths"//
	};

	String[] sqlQueries() default {};

	String[] sqlFilePaths() default {};

	String[] springContextPaths() default {};

	String[] xmlFilePaths() default {};

	String[] xlsFilePaths() default {};

	String[] xlsxFilePaths() default {};

	Class<? extends FixtureBuilder>[] builders() default {};

	Class[] cleaning() default {};
}
