package crazy_wrapper.Crazy;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangy on 2017/6/13.
 * converter to convert inputstream from web response.
 */

public class InputStreamConverter<T> implements  ResponseConverter<InputStream,T>{

    @Override public T convert(InputStream value) throws IllegalStateException, IOException {
        return null;
    }
}
