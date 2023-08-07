/*
 * Copyright (C) 2012 Square, Inc.
 *
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
 */
package crazy_wrapper.Crazy;

import java.io.IOException;

/**
 * Convert objects to and from their representation in HTTP. Instances are created by {@linkplain
 * Factory a factory} which is {@linkplain RequestBuilder#convertFactory(Factory) installed}
 * into the {@link crazy_wrapper.Crazy.request.CrazyRequest} instance.
 */
public interface ResponseConverter<F, T> {

    T convert(F value) throws CrazyException,IOException;

    abstract class Factory {
        /**
         * Returns a {@link ResponseConverter} for converting an HTTP response body to {@code type}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for
         * response types such as {@code SimpleResponse} from a {@code Call<SimpleResponse>}
         * declaration.
         */

        public <F,T> ResponseConverter<F, T> responseBodyConverter() throws CrazyException{
            return null;
        }
    }
}
