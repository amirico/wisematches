package wisematches.client.gwt.core.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public final class JSArray<T> implements Iterable<T> {
    private final JavaScriptObject object;

    public JSArray(int length) {
        this(newJSArray(length));
    }

    public JSArray(T[] ts) {
        this(newJSArray(ts.length));

        for (int i = 0; i < ts.length; i++) {
            set(i, ts[i]);
        }
    }

    public JSArray(JavaScriptObject object) {
        this.object = object;
    }

    public T get(int index) {
        return (T) get(object, index);
    }

    public void set(int index, T t) {
        set(object, index, t);
    }

    public int length() {
        return length(object);
    }

    public JavaScriptObject getJavaScriptObject() {
        return object;
    }

    private static native JavaScriptObject newJSArray(int length) /*-{
        return new Array(length);
    }-*/;

    public static native int length(JavaScriptObject array) /*-{
        return array.length;
    }-*/;

    public static native <T> T get(JavaScriptObject array, int index) /*-{
        return array[index];
    }-*/;

    public static native void set(JavaScriptObject array, int index, Object value) /*-{
        array[index] = value;
    }-*/;


    public List<T> asList() {
        return new ArrayList<T>();
    }
    
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index;

            public boolean hasNext() {
                return index < length();
            }

            public T next() {
                return get(index++);
            }

            public void remove() {
                throw new UnsupportedOperationException("This iterator is read-only");
            }
        };
    }
}