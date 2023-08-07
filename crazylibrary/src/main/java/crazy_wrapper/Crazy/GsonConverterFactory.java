/*
 * Copyright (C) 2015 Square, Inc.
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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public final class GsonConverterFactory<T> extends ResponseConverter.Factory {


  public static <T> GsonConverterFactory create(Type type) {
    return create(new Gson(),type);
  }

  /**
   * Create an instance using {@code gson} for conversion. Encoding to JSON and
   * decoding from JSON (when no charset is specified by a header) will use UTF-8.
   */
  public static <T> GsonConverterFactory create(Gson gson,Type type) {
    return new GsonConverterFactory(gson,type);
  }

  private final Gson gson;
  private final Type type;

  private GsonConverterFactory(Gson gson,Type type) {
    if (gson == null) throw new NullPointerException("gson == null");
    this.gson = gson;
    this.type = type;
  }

  @Override public <F, T> ResponseConverter<F, T> responseBodyConverter() throws CrazyException {
    if (type == null){
      throw new IllegalArgumentException("request result bean type havent been set");
    }
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new StringConvert(gson, adapter);
  }
}
