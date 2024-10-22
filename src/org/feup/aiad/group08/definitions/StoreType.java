package org.feup.aiad.group08.definitions;

public enum StoreType {
    
    TECH("Tech-store"),
    FOOD("Food-store"),
    CLOTHES("Clothes-store"),
    FURNITURE("Furniture-store"),
    BOOK("Books-store"),
    GAME("Game-store");

    private final String type;

    StoreType(final String type) {
        this.type = type;
    }

    @Override 
    public String toString() {
        return type;
    }
    
}
