import org.apache.commons.lang3.reflect.MethodUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.internal.cglib.proxy.Callback;
import org.assertj.core.internal.cglib.proxy.CallbackFilter;
import org.assertj.core.internal.cglib.proxy.Enhancer;
import org.assertj.core.internal.cglib.proxy.MethodInterceptor;
import org.assertj.core.internal.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.util.Arrays.array;

public class MainAssert {

    public static void main(String[] args) {
        SoftProxies proxies = new SoftProxies();
        proxies.create(BooleanAssert.class, Boolean.class, true).as("Test de sandra").isTrue();
        proxies.create(BooleanAssert.class, Boolean.class, false).as("Test de gabriel").isTrue();
        proxies.create(BooleanAssert.class, Boolean.class, true).as("Test de sandra").isTrue();
        proxies.create(BooleanAssert.class, Boolean.class, false).as("Test de gabriel2").isTrue();
        proxies.collector.errors().forEach(System.out::println);
    }

    static class ErrorCollector implements MethodInterceptor {

        private final List<Throwable> errors = new ArrayList<>();

        Method descMethod;

        public ErrorCollector() {
            super();

            descMethod = MethodUtils.getAccessibleMethod(AbstractAssert.class, "describedAs", String.class, Object[].class);
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            try {
                if (method.equals(descMethod)) {
                    proxy.invokeSuper(obj, args);

                } else {
                    proxy.invokeSuper(obj, args);
                }

            } catch (AssertionError e) {
                //throw e;
                errors.add(e);
            }
            return obj;
        }

        public List<Throwable> errors() {
            return Collections.unmodifiableList(errors);
        }
    }

    static class SoftProxies {

        private final ErrorCollector collector = new ErrorCollector();

        List<Throwable> errorsCollected() {
            return collector.errors();
        }

        @SuppressWarnings("unchecked")
        <V, T> V create(Class<V> assertClass, Class<T> actualClass, T actual) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(assertClass);
            enhancer.setCallbackFilter(CollectErrorsOrCreateExtractedProxy.FILTER);
            enhancer.setCallbacks(new Callback[] { collector });
            return (V) enhancer.create(array(actualClass), array(actual));
        }

        private enum CollectErrorsOrCreateExtractedProxy implements CallbackFilter {
            FILTER;

            private static final int ERROR_COLLECTOR_INDEX = 0;
            private static final int PROXIFY_EXTRACTING_INDEX = 1;

            public int accept(Method method) {
                return isExtractingMethod(method) ? PROXIFY_EXTRACTING_INDEX : ERROR_COLLECTOR_INDEX;
            }

            private boolean isExtractingMethod(Method method) {
                return method.getName().toLowerCase().contains("extracting");
            }
        }
    }
}
