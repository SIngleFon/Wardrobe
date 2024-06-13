package ru.wardrobe.model.enums;
/**
 * Перечисление для размеров одежды.
 */
public enum EnumSize {
    XXXS("38"),
    XXS("40"),
    XS("42"),
    S("44-46"),
    M("48-50"),
    L("52"),
    XL("54-56"),
    XXL("58"),
    XXXL("60"),
    NONE("None");

    private final String value;

    EnumSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}