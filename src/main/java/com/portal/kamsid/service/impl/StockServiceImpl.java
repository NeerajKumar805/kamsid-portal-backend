package com.portal.kamsid.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.portal.kamsid.dto.StockDirection;
import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.DailyStockMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.entity.ProductDetails;
import com.portal.kamsid.entity.ProductInventory;
import com.portal.kamsid.entity.StockEntry;
import com.portal.kamsid.exception.InsufficientStockException;
import com.portal.kamsid.repository.ProductInventoryRepository;
import com.portal.kamsid.repository.StockEntryRepository;
import com.portal.kamsid.service.StockService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final ProductInventoryRepository inventoryRepo;
    private final StockEntryRepository entryRepo;

    private final boolean allowNegativeStock = false;

    @Override
    @Transactional
    public void recordProduction(DailyProductionMaster master) {
        for (ProductDetails pd : master.getProducts()) {
            BigDecimal qty = safeQty(pd.getQuantity());
            applyInventoryChange(pd.getProduct(), qty, StockDirection.IN, "PRODUCTION", master.getId(), pd.getPdId(), pd.getDate(), pd.getRemark());
        }
    }

    @Override
    @Transactional
    public void recordSale(DailySaleMaster master) {
        for (ProductDetails pd : master.getProducts()) {
            BigDecimal qty = safeQty(pd.getQuantity());
            // reduce inventory
            applyInventoryChange(pd.getProduct(), qty, StockDirection.OUT, "SALE", master.getId(), pd.getPdId(), pd.getDate(), pd.getRemark());
        }
    }

    @Override
    @Transactional
    public void recordManualStock(DailyStockMaster master) {
        for (ProductDetails pd : master.getProducts()) {
            BigDecimal qty = safeQty(pd.getQuantity());
            // Manual purchase is an IN operation
            applyInventoryChange(pd.getProduct(), qty, StockDirection.IN, "STOCK", master.getId(), pd.getPdId(), pd.getDate(), pd.getRemark());
        }
    }

    private BigDecimal safeQty(BigDecimal q) {
        return q == null ? BigDecimal.ZERO : q;
    }

    private void applyInventoryChange(Product product, BigDecimal qty, StockDirection direction,
                                      String sourceModule, Long masterId, Long pdId, LocalDate date, String remark) {
        // lock inventory row
        ProductInventory inv = inventoryRepo.findByProduct(product)
            .orElseGet(() -> ProductInventory.builder()
                .product(product)
                .quantity(BigDecimal.ZERO)
                .lastUpdated(date != null ? date : LocalDate.now(ZoneId.of("Asia/Kolkata")))
                .build());

        // since findByProduct is PESSIMISTIC_WRITE, either we got a managed entity locked or we will create and save below.
        if (inv.getId() == null) {
            // new row - persist immediately so next concurrent tx sees it
            inv = inventoryRepo.save(inv);
        }

        BigDecimal newQty = direction == StockDirection.IN ? inv.getQuantity().add(qty) : inv.getQuantity().subtract(qty);

        if (!allowNegativeStock && newQty.compareTo(BigDecimal.ZERO) < 0) {
        	throw new InsufficientStockException(
                    "Insufficient stock for product " + product.getPid() + " (" + product.getProductName() + "). Available: "
                    + inv.getQuantity() + ", required: " + qty);
        }

        inv.setQuantity(newQty);
        inv.setLastUpdated(date != null ? date : LocalDate.now(ZoneId.of("Asia/Kolkata")));
        inventoryRepo.save(inv);

        StockEntry se = StockEntry.builder()
                .product(product)
                .quantity(qty)
                .direction(direction)
                .sourceModule(sourceModule)
                .referenceMasterId(masterId)
                .referenceProductDetailsId(pdId)
                .entryDate(date != null ? date : LocalDate.now(ZoneId.of("Asia/Kolkata")))
                .remark(remark)
                .build();
        entryRepo.save(se);
    }
}
