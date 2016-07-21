package org.osgl.genie.loader;

import org.osgl.$;
import org.osgl.Osgl;
import org.osgl.genie.BeanSpec;
import org.osgl.genie.Genie;
import org.osgl.genie.InjectException;
import org.osgl.genie.annotation.TypeOf;
import org.osgl.util.C;
import org.osgl.util.E;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Load beans whose class extends/implements specified class/interface (as hint)
 */
public abstract class TypedElementLoader<T> extends ElementLoaderBase<T> {

    /**
     * This method will load instances of all public and non-abstract classes that
     * implements/extends the interface/class specified as `value` option
     *
     * @param options   must contains an entry indexed with "value" and the value should be a Class type
     * @param container the bean spec of the container into which the element will be loaded
     * @param genie     the dependency injector
     * @return the list of bean instances whose class is sub class or implementation of `hint`
     */
    @Override
    public final Iterable<T> load(Map<String, Object> options, BeanSpec container, final Genie genie) {
        List<Class<? extends T>> classes = load(targetClass(options, container));
        return C.list(classes).map(new $.Transformer<Class<? extends T>, T>() {
            @Override
            public T transform(Class<? extends T> aClass) {
                return genie.get(aClass);
            }
        });
    }

    /**
     * This method returns a predicate function that test the bean instance against the
     * class specified by `hint`. If the bean is an instance of the `hint` class, then
     * the predicate function returns `true` otherwise it returns `false`
     *
     * @param options   must contains an entry indexed with "value" and the value should be a Class type
     * @param container the bean spec of the container into which the element will be loaded
     * @return a predicate function whose behavior is described above
     */
    @Override
    public final Osgl.Function<T, Boolean> filter(Map<String, Object> options, BeanSpec container) {
        final Class baseClass = targetClass(options, container);
        return new $.Predicate<T>() {
            @Override
            public boolean test(T o) {
                return baseClass.isAssignableFrom(o.getClass());
            }
        };
    }


    /**
     * Load a list of beans whose class is type or implementation of the
     * specified `type`
     *
     * @param type the class or interface specification
     * @return a list of beans as described
     */
    protected abstract List<Class<? extends T>> load(Class<T> type);

    private Class<T> targetClass(Map<String, Object> options, BeanSpec container) {
        Object hint = options.get("value");
        E.illegalArgumentIf(!(hint instanceof Class));
        Class<?> targetClass = $.cast(hint);
        if (TypeOf.PlaceHolder.class == targetClass) {
            List<Type> types = container.typeParams();
            if (!types.isEmpty()) {
                // the effective type is always the last one
                // this is for both Collection and Map
                Type type = types.get(types.size() - 1);
                if (type instanceof Class) {
                    targetClass = $.cast(type);
                }
            }
            if (TypeOf.PlaceHolder.class == targetClass) {
                throw new InjectException("Cannot load element - target type info is missing");
            }
        }
        return $.cast(targetClass);
    }

}
