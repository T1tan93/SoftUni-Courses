package furnitureFactory.entities.factories;

import furnitureFactory.entities.workshops.Workshop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static furnitureFactory.common.ExceptionMessages.FACTORY_NAME_NULL_OR_EMPTY;

public abstract class BaseFactory implements Factory {

    private String name;
    private Collection<Workshop> workshops;
    private Collection<Workshop> removedWorkshops;

    public BaseFactory(String name) {
        setName(name);
        workshops = new ArrayList<>();
        removedWorkshops = new ArrayList<>();
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new NullPointerException(FACTORY_NAME_NULL_OR_EMPTY);
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addWorkshop(Workshop workshop) {
        workshops.add(workshop);

    }

    @Override
    public Collection<Workshop> getWorkshops() {
        return new ArrayList<>(workshops);
    }

    @Override
    public Collection<Workshop> getRemovedWorkshops() {
        return new ArrayList<>(removedWorkshops);
    }
}
