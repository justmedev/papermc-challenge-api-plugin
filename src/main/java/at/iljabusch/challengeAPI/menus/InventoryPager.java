package at.iljabusch.challengeAPI.menus;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A paged inventory is an inventory with pages. It works using pagedSections, so you can partially page an inventory (for example left half is paged, right half has static functions)
 */
@Getter
public class InventoryPager<ITEM extends InventoryItem> {

  private static final int INVENTORY_NUM_COLS = 9;
  private final ArrayList<HashMap<Integer, ITEM>> pages = new ArrayList<>();
  @Getter
  protected InventoryHolder inventoryHolder;
  /**
   * The page you are currently viewing, is always an index of the {@link #pages} list.
   */
  private int currentPageIndex = 0;
  /**
   * The amount of cols from the left that are affected by this class (paged)
   */
  private int pagedSectionCols = 9;
  /**
   * The amount of rows from the top that are affected by this class (paged)
   */
  private int pagedSectionRows = 3;

  public InventoryPager(InventoryHolder inventoryHolder) {
    this.inventoryHolder = inventoryHolder;
  }

  public HashMap<Integer, ITEM> getCurrentPage() {
    return this.pages.get(this.currentPageIndex);
  }

  /**
   * This sets the part of the inventory that should be treated as paged and managed by this class
   *
   * @param cols amount of columns from the left
   * @param rows amount of rows from the top
   * @throws InventoryDimensionException when the cols/rows are larger than the inventories cols/rows
   */
  public void setPagedSectionDimensions(int cols, int rows) throws InventoryDimensionException {
    if (cols > 9 || rows > inventoryHolder.getInventory().getSize() / 9) {
      throw new InventoryDimensionException(
          "The paged inventory section cannot be langer than the inventory itself!");
    }
    this.pagedSectionCols = cols;
    this.pagedSectionRows = rows;
  }

  public void previousPage() {
    if (currentPageIndex <= 0) {
      return;
    }

    this.currentPageIndex--;
    drawPage();
    drawPageArrows();
  }

  public void nextPage() {
    if (pages.size() - 1 <= currentPageIndex) {
      return;
    }

    this.currentPageIndex++;
    drawPage();
    drawPageArrows();
  }

  /**
   * Convert a global (non-sectionized) index to a sectionized index (an index, that skips reserved spaces)
   *
   * @param globalIndex the non-sectioned index
   * @return index, that skips spaces not allowed in {@link #pagedSectionCols}
   */
  public int getSectionIndex(int globalIndex) {
    var numNonPagedCols = INVENTORY_NUM_COLS - pagedSectionCols;
    var completedRows = Math.floorDiv(globalIndex, pagedSectionCols);
    var offset = completedRows * numNonPagedCols;
    return globalIndex + offset;
  }

  public void drawPage() {
    var page = getCurrentPage();
    for (int i = 0; i < this.pagedSectionCols * this.pagedSectionRows; i++) {
      var item = page.get(getSectionIndex(i));
      this.inventoryHolder.getInventory().setItem(
          getSectionIndex(i),
          item == null ? new ItemStack(Material.AIR) : item.getItemStack()
      );
    }
  }

  public boolean hasPreviousPage() {
    return this.currentPageIndex > 0;
  }

  public boolean hasNextPage() {
    return this.pages.size() - 1 > this.currentPageIndex;
  }

  public void drawPageArrows() {
    var inv = inventoryHolder.getInventory();
    if (hasPreviousPage()) {
      inv.setItem(inv.getSize() - 2, InventoryUtils.getSkull(SkullTextures.arrowLeft));
    } else {
      inv.setItem(inv.getSize() - 2, new ItemStack(Material.AIR));
    }

    if (hasNextPage()) {
      inv.setItem(inv.getSize() - 1, InventoryUtils.getSkull(SkullTextures.arrowRight));
    } else {
      inv.setItem(inv.getSize() - 1, new ItemStack(Material.AIR));
    }
  }

  /**
   * Automatically assign too large indexes for one page to the next page!
   *
   * @param allItems map of an inventory layout, larger than the actual inventory
   */
  public void autoPage(HashMap<Integer, ITEM> allItems) {
    var maxIndexPerPage = (pagedSectionCols * pagedSectionRows) - 1;
    // TODO: This implementation does not account for empty slots in between
    allItems.forEach((index, item) -> {
      var pageIndex = 0;
      while (index > maxIndexPerPage) {
        index -= maxIndexPerPage + 1;
        pageIndex++;
      }

      while (this.pages.size() < pageIndex + 1) {
        try {
          this.pages.get(pageIndex);
        } catch (IndexOutOfBoundsException e) {
          this.pages.add(new HashMap<>());
        }
      }

      this.pages.get(pageIndex).put(getSectionIndex(index), item);
    });
  }
}
