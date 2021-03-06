package org.osgl.inject;

/*-
 * #%L
 * OSGL Genie
 * %%
 * Copyright (C) 2017 OSGL (Open Source General Library)
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

import org.osgl.inject.annotation.RequestScoped;
import org.osgl.inject.annotation.SessionScoped;

import javax.inject.Singleton;

public class GuiceAdaptor implements GeniePlugin {
    @Override
    public void register(Genie genie) {
        genie.registerScopeAlias(Singleton.class, com.google.inject.Singleton.class);
        genie.registerScopeAlias(SessionScoped.class, com.google.inject.servlet.SessionScoped.class);
        genie.registerScopeAlias(RequestScoped.class, com.google.inject.servlet.RequestScoped.class);
        // TODO support Guice Module
    }
}
