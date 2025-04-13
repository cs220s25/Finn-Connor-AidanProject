package edu.moravian;
import java.util.HashSet;
import java.util.Set;

public class Location {
    private String description;
    private Set<String> connectedLocations;
    private String item;
    private boolean hasMonster;

    public Location(String description) {
        this.description = description;
        this.connectedLocations = new HashSet<>();
        this.item = null;
        this.hasMonster = false;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getConnectedLocations() {
        return connectedLocations;
    }

    public void addConnectedLocation(String locationName) {
        connectedLocations.add(locationName);
    }

    public void setConnectedLocations(Set<String> connections) {
        connectedLocations.clear();
        connectedLocations.addAll(connections);
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setMonster(boolean hasMonster) {
        this.hasMonster = hasMonster;
    }

    public boolean hasMonster() {
        return hasMonster;
    }
}