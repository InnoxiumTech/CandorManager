package uk.co.innoxium.candor.mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;


/**
 * A Class that handles all the Mods list.
 */
public class ModList<Mod> {

    // Our Mods ArrayList
    private final ArrayList<Mod> MODS_LIST = new ArrayList<>();
    // An array of listeners for the Mods list.
    private final ArrayList<ListChangeListener<Mod>> listeners = new ArrayList<>();

    /**
     * Adds a mod to the list, then fires an event to the
     *
     * @param mod - The mod to add to the list
     * @return - True if it was added, false, if not
     */
    public boolean add(Mod mod) {

        boolean result = MODS_LIST.add(mod);

        fireChangeToListeners("add", mod, result);

        return result;
    }

    /**
     * Removes a mod from the list, then fires an event to all listeners.
     *
     * @param mod - The mod to remove
     * @return - true for removed, false if not.
     */
    public boolean remove(Mod mod) {

        boolean result = MODS_LIST.remove(mod);

        fireChangeToListeners("remove", mod, result);

        return result;
    }

    /**
     * Adds a collection of mods to the list, then fires an event to the listeners.
     *
     * @param c - The collection to add.
     * @return - true if they were added, false if not.
     */
    public boolean addAll(Collection<? extends Mod> c) {

        boolean result = MODS_LIST.addAll(c);

        fireChangeToListeners("add-all", c, result);

        return result;
    }

    /**
     * Fires an event to all listeners
     *
     * @param identifier - a unique identifier for the action being taken.
     * @param mod        - The mod that had an action taken upon it.
     * @param result     - The result of the initial action
     */
    public void fireChangeToListeners(String identifier, Mod mod, boolean result) {

        listeners.forEach(listener -> listener.handleChange(identifier, mod, result));
    }

    /**
     * Fires an event to all listeners
     *
     * @param identifier - a unique identifier for the action being taken.
     * @param c          - The collection of mods that had an action taken upon it.
     * @param result     - The result of the initial action
     */
    public void fireChangeToListeners(String identifier, Collection<? extends Mod> c, boolean result) {

        listeners.forEach(listener -> listener.handleChange(identifier, c, result));
    }

    /**
     * Converts the Mods List to a primitive array
     *
     * @return - An array of mods
     */
    public Mod[] toArray() {

        return (Mod[]) MODS_LIST.toArray();
    }

    /**
     * Adds a listener to the list of listeners
     *
     * @param listener - The listener to add.
     */
    public void addListener(ListChangeListener<Mod> listener) {

        if(!listeners.contains(listener)) listeners.add(listener);
    }

    /**
     * A thin wrapper for the forEach method of the List
     *
     * @param action - the action to take
     */
    public void forEach(Consumer<? super Mod> action) {

        MODS_LIST.forEach(action);
    }

    /**
     * Clears the List
     */
    public void clear() {

        MODS_LIST.clear();
    }

    /**
     * An interface that allows you to act upon a change.
     */
    public interface ListChangeListener<T> {

        void handleChange(String identifier, T mod, boolean result);

        void handleChange(String identifier, Collection<? extends T> c, boolean result);
    }
}
