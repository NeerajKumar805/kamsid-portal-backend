package com.portal.kamsid.service;

import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.DailyStockMaster;

public interface StockService {
	void recordProduction(DailyProductionMaster master);

	void recordSale(DailySaleMaster master);

	void recordManualStock(DailyStockMaster master);
}
