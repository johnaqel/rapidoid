package org.rapidoid.render;

/*
 * #%L
 * rapidoid-commons
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
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

import javassist.*;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.u.U;

import java.util.concurrent.atomic.AtomicInteger;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class TemplateCompiler {

	public static final AtomicInteger ID_GEN = new AtomicInteger();

	public static TemplateRenderer compile(XNode node) {
		try {
			String source = TemplateToCode.generate(node);
			return tryToCompile(source);

		} catch (NotFoundException e) {
			throw U.rte(e);
		} catch (CannotCompileException e) {
			throw U.rte(e);
		} catch (Exception e) {
			throw U.rte(e);
		}
	}

	private static TemplateRenderer tryToCompile(String source) throws NotFoundException, CannotCompileException,
			InstantiationException, IllegalAccessException {

		ClassPool cp = ClassPool.getDefault();
		CtClass sup = cp.get(Object.class.getCanonicalName());
		CtClass cls = cp.makeClass("RapidoidTemplate" + ID_GEN.incrementAndGet(), sup);

		cls.addInterface(cp.get(TemplateRenderer.class.getCanonicalName()));
		cls.addConstructor(CtNewConstructor.defaultConstructor(cls));

		CtClass[] params = {cp.get(RenderCtx.class.getCanonicalName())};
		CtClass clsVoid = cp.get(void.class.getCanonicalName());
		cls.addMethod(CtNewMethod.make(Modifier.PUBLIC, clsVoid, "render", params, new CtClass[0], source, cls));

		return (TemplateRenderer) cls.toClass().newInstance();
	}

}