package crazy_wrapper.Crazy;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.*;

import crazy_wrapper.Crazy.Utils.Utils;

/**
 * Created by Administrator on 2017/6/9.
 */

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


final class StringConvert<T> implements ResponseConverter<String, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    StringConvert(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(String value) throws CrazyException, IOException {
        InputStream inputStream = null;
        Reader reader = null;
        JsonReader jsonReader = null;
        try {
            inputStream = new ByteArrayInputStream(value.getBytes());
            reader = new InputStreamReader(inputStream);
            jsonReader = gson.newJsonReader(reader);
            jsonReader.setLenient(true);
            return adapter.read(jsonReader);
        } catch (IllegalStateException e) {
            throw new CrazyException(e.getMessage(), e);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (reader != null)
                reader.close();
            if (jsonReader != null)
                jsonReader.close();
        }
    }
}

