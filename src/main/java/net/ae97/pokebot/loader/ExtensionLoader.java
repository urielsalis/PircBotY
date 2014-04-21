package net.ae97.pokebot.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.ae97.pokebot.extension.Extension;

public class ExtensionLoader extends URLClassLoader {

    private final ExtensionPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();

    ExtensionLoader(ExtensionPluginLoader l, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        loader = l;
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        Class<?> result = classes.get(name);
        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }
            if (result == null) {
                result = Class.forName(name, true, this);
            }
            if (result != null) {
                loader.setClass(name, result);
            }
        }
        if (result != null) {
            classes.put(name, result);
        }
        return result;
    }

    public Class<? extends Extension> findMainClass() {
        for (Class<?> cl : classes.values()) {
            if (Extension.class.isAssignableFrom(cl)) {
                return cl.asSubclass(Extension.class);
            }
        }
        return null;
    }

    public Set<Class<? extends Extension>> findMainClasses() {
        Set<Class<? extends Extension>> classList = new HashSet<>();
        for (Class<?> cl : classes.values()) {
            if (Extension.class.isAssignableFrom(cl)) {
                classList.add(cl.asSubclass(Extension.class));
            }
        }
        return classList;
    }
}