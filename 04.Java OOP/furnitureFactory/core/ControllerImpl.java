package furnitureFactory.core;

import furnitureFactory.entities.factories.AdvancedFactory;
import furnitureFactory.entities.factories.BaseFactory;
import furnitureFactory.entities.factories.Factory;
import furnitureFactory.entities.factories.OrdinaryFactory;
import furnitureFactory.entities.wood.OakWood;
import furnitureFactory.entities.wood.Wood;
import furnitureFactory.entities.workshops.DeckingWorkshop;
import furnitureFactory.entities.workshops.TableWorkshop;
import furnitureFactory.entities.workshops.Workshop;
import furnitureFactory.repositories.WoodRepository;
import furnitureFactory.repositories.WoodRepositoryImpl;
import furnitureFactory.repositories.WorkshopRepository;
import furnitureFactory.repositories.WorkshopRepositoryImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static furnitureFactory.common.ConstantMessages.*;
import static furnitureFactory.common.ExceptionMessages.*;

public class ControllerImpl implements Controller {


    private WoodRepository woodRepository;
    private WorkshopRepository workshopRepository;
    private Collection<Factory> factories;

    public ControllerImpl() {
        this.woodRepository = new WoodRepositoryImpl();
        this.workshopRepository = new WorkshopRepositoryImpl();
        this.factories = new ArrayList<>();
    }

    @Override
    public String buildFactory(String factoryType, String factoryName) {
        if (factories.stream().anyMatch(f -> f.getName().equals(factoryName))) {
            throw new NullPointerException("Factory with this name already exists.");
        }

        Factory factory;
        switch (factoryType) {
            case "OrdinaryFactory":
                factory = new OrdinaryFactory(factoryName);
                break;
            case "AdvancedFactory":
                factory = new AdvancedFactory(factoryName);
                break;
            default:
                throw new IllegalArgumentException("Invalid factory type.");
        }

        factories.add(factory);
        return String.format("Successfully build %s %s.", factoryType, factoryName);
    }

    @Override
    public Factory getFactoryByName(String factoryName) {
        return factories.stream()
                .filter(f -> f.getName().equals(factoryName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String buildWorkshop(String workshopType, int woodCapacity) {
        Workshop workshop;

        switch (workshopType) {
            case "TableWorkshop":
                workshop = new TableWorkshop(woodCapacity);
                break;
            case "DeckingWorkshop":
                workshop = new DeckingWorkshop(woodCapacity);
                break;
            default:
                throw new IllegalArgumentException("Invalid workshop type.");
        }

        workshopRepository.add(workshop);
        return String.format("Successfully build workshop of type %s.", workshopType);
    }

    @Override
    public String addWorkshopToFactory(String factoryName, String workshopType) {
        Factory factory = getFactoryByName(factoryName);
        Workshop workshop = workshopRepository.findByType(workshopType);

        if (workshop == null) {
            throw new NullPointerException(String.format("There is no workshop of type %s in repository.", workshopType));
        }

        if (factory.getWorkshops().stream().anyMatch(w -> w.getClass().getSimpleName().equals(workshopType))) {
            throw new IllegalArgumentException("Workshop of this type already exists in this factory.");
        }

        if ((factory instanceof OrdinaryFactory && workshop instanceof DeckingWorkshop) ||
                (factory instanceof AdvancedFactory && workshop instanceof TableWorkshop)) {
            return String.format("This factory does not support this type of workshop.");
        }

        factory.addWorkshop(workshop);
        return String.format("Successfully added workshop of type %s in %s.", workshopType, factoryName);
    }

    @Override
    public String buyWoodForFactory(String woodType) {
        Wood wood;
        if ("OakWood".equals(woodType)) {
            wood = new OakWood();
        } else {
            throw new IllegalArgumentException("Invalid wood type.");
        }
        woodRepository.add(wood);
        return String.format("Successfully bought %s.", woodType);
    }

    @Override
    public String addWoodToWorkshop(String factoryName, String workshopType, String woodType) {
        Factory factory = getFactoryByName(factoryName);
        Wood wood = woodRepository.findByType(woodType);

        if (factory.getWorkshops().isEmpty()) {
            throw new NullPointerException("There are no added workshops to add wood to.");
        }

        if (wood == null) {
            throw new NullPointerException(String.format("There is no %s in wood repository.", woodType));
        }

        Workshop workshop = factory.getWorkshops().stream()
                .filter(w -> w.getClass().getSimpleName().equals(workshopType))
                .findFirst()
                .orElse(null);

        if (workshop != null) {
            workshop.addWood(wood);
            woodRepository.remove(wood);
            return String.format("Successfully added %s to %s.", woodType, workshopType);
        } else {
            return String.format("There is no %s in %s.", workshopType, factoryName);
        }
    }


    @Override
    public String produceFurniture(String factoryName) {
        Factory factory = getFactoryByName(factoryName);

        if (factory.getWorkshops().isEmpty()) {
            throw new NullPointerException("There are no added workshops to build furniture.");
        }

        for (Workshop workshop : factory.getWorkshops()) {
            workshop.produce();
        }

        return String.format("Successfully produced furniture at %s.", factoryName);
    }

    @Override
    public String getReport() {
        StringBuilder statistics = new StringBuilder();
        for (Factory factory : factories) {
            statistics.append("Production by ").append(factory.getName()).append(" factory:\n");
            Collection<Workshop> workshops = factory.getWorkshops();
            if (workshops.isEmpty()) {
                statistics.append("  No workshops were added to produce furniture.\n");
            } else {
                for (Workshop workshop : workshops) {
                    statistics.append("  ").append(workshop.getClass().getSimpleName())
                            .append(": ").append(workshop.getProducedFurnitureCount())
                            .append(" furniture produced\n");
                }
            }
        }
        return statistics.toString().trim();
    }
}
