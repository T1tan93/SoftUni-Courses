package furnitureFactory.entities.workshops;

import furnitureFactory.entities.wood.Wood;

public abstract class BaseWorkshop implements Workshop {

    private int woodQuantity;
    private int producedFurnitureCount;
    private int woodQuantityReduceFactor;

    public BaseWorkshop(int woodQuantity, int woodQuantityReduceFactor) {
        this.woodQuantity = woodQuantity < 0 ? 0 : woodQuantity;
        this.woodQuantityReduceFactor = woodQuantityReduceFactor;
        this.producedFurnitureCount = 0;
    }

    @Override
    public void addWood(Wood wood) {
        this.woodQuantity += wood.getWoodQuantity();
    }

    @Override
    public void produce() {
        if (this.woodQuantity >= this.woodQuantityReduceFactor) {
            this.woodQuantity -= this.woodQuantityReduceFactor;
            this.producedFurnitureCount++;
        } else {
            this.woodQuantity = 0;  // Reduce to zero if not enough wood
        }
    }

    public int getProducedFurnitureCount() {
        return this.producedFurnitureCount;
    }

    public int getWoodQuantity() {
        return this.woodQuantity;
    }
}
