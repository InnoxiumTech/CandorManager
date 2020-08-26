package uk.co.innoxium.candor.mod;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ModsHandler {

    public static final ModList MODS = new ModList<Mod>();

    public static class ModList<Mod> {

        private ArrayList<Mod> MODS_LIST = Lists.newArrayList();
        private ArrayList<ListChangeListener> listeners = Lists.newArrayList();

        public boolean add(Mod mod) {

            boolean result = MODS_LIST.add(mod);

            fireChangeToListeners("add", mod, result);

            return result;
        }

        public boolean remove(Mod mod) {

            boolean result = MODS_LIST.remove(mod);

            fireChangeToListeners("remove", mod, result);

            return result;
        }

        public boolean addAll(Collection<? extends Mod> c) {

            boolean result = MODS_LIST.addAll(c);

            fireChangeToListeners("add-all", c, result);

            return result;
        }

        public void fireChangeToListeners(String identifier, Mod mod, boolean result) {

            listeners.forEach(listener -> listener.handleChange(identifier, mod, result));
        }

        public void fireChangeToListeners(String identifier, Collection<? extends Mod> c, boolean result) {

            listeners.forEach(listener -> listener.handleChange(identifier, c, result));
        }

        public Mod[] toArray() {

            return (Mod[]) MODS_LIST.toArray();
        }

        public void addListener(ListChangeListener<Mod> listener) {

            if(!listeners.contains(listener)) listeners.add(listener);
        }

        public void forEach(Consumer action) {

            MODS_LIST.forEach(action);
        }
    }

    public interface ListChangeListener<T> {

        void handleChange(String identifier, T mod, boolean result);
        void handleChange(String identifier, Collection<? extends T> c, boolean result);
    }
}
