package org.feup.aiad.group08.definitions;

public enum StoreType {
    
    TECH("Tech-store"),
    FOOD("Food-store"),
    CLOTHES("Clothes-store"),
    FURNITURE("Furniture-store"),
    BOOKS("Books-store"),
    GAMES("Game-store");

    private final String type;

    StoreType(final String type) {
        this.type = type;
    }

    // public static StoreType fromString(String value){
    //     return new StoreType(value);
    // }

    @Override 
    public String toString() {
        return type;
    }
    
}
