package me.shadowchild.candor.settings;

public class Setting<T> {

    private String category, name, comment;
    private T value;

    /**
     * Created a new Setting and adds it to the category
     *
     * @param category - the category for the setting, can have subcategories formatted like this: "category.sub"
     * @param name - the name of the setting
     * @param comment - a comment to put in the config, can be null for no comment
     * @param defaultValue - the default value to set the value of the setting to
     */
    public Setting(String category, String name, String comment, T defaultValue) {

        this.category = category;
        this.name = name;
        this.comment = comment;
        this.value = defaultValue;
    }

    public Setting(String category, String name, T defaultValue) {

        this(category, name, null, defaultValue);
    }

    public String getCategory() {

        return category;
    }

    public String getName() {

        return name;
    }

    public String getComment() {

        return comment;
    }

    public T get() {

        return value;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", value=" + value +
                '}';
    }
}
