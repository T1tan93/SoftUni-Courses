package furnitureFactory.repositories;

import furnitureFactory.entities.workshops.Workshop;

import java.util.ArrayList;
import java.util.Collection;

public class WorkshopRepositoryImpl implements WorkshopRepository {

    private Collection<Workshop> workshops;

    public WorkshopRepositoryImpl() {
        this.workshops = new ArrayList<>();
    }

    @Override
    public void add(Workshop workshop) {
        if (this.workshops.stream().anyMatch(w -> w.getClass().equals(workshop.getClass()))) {
            throw new IllegalArgumentException("Workshop of this type already exists in the repository.");
        }
        if (workshop.getWoodQuantity() <= 0) {
            throw new IllegalArgumentException("Can not build workshop with zero or less wood quantity.");
        }
        this.workshops.add(workshop);
    }

    @Override
    public boolean remove(Workshop workshop) {
        return this.workshops.remove(workshop);
    }

    @Override
    public Workshop findByType(String type) {
        return this.workshops.stream()
                .filter(w -> w.getClass().getSimpleName().equals(type))
                .findFirst()
                .orElse(null);
    }
}