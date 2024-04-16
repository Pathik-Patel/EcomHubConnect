package com.ecomhubconnect.EcomHubConnect.Repo;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.Month;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecomhubconnect.EcomHubConnect.Entity.Orders;
import com.ecomhubconnect.EcomHubConnect.Entity.Stores;
import com.ecomhubconnect.EcomHubConnect.Entity.Users;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	Optional<Orders> findFirstByStoreOrderByLastmodifiedDateDesc(Stores store);

	Optional<Orders> findByOrderidAndStore(int orderId, Stores store);
	
	List<Orders> findByStore(Stores store);
	
//	List<Orders> findByStoreAndOrderCreatedDateAfter(Stores store, Timestamp orderCreatedDate);
	
	List<Orders> findByStoreAndOrderCreatedDateBetween(Stores store, Timestamp startDate, Timestamp endDate);
	
	@Query(value = "SELECT "
	        + "COUNT(*) AS totalOrders, "
	        + "SUM(CAST(REPLACE(o.total, ',', '') AS FLOAT)) AS totalSale "
	        + "FROM Orders o "
	        + "WHERE o.store.storeid = :storeId "
	        + "AND o.orderCreatedDate BETWEEN :startDate AND :endDate")
	Map<String, Object> getTotalOrdersAndSale(@Param("storeId") int storeId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
	
	@Query(value = "SELECT o.state, COUNT(o.state) AS stateCount, COUNT(*) AS totalOrdersCount, "
			+ "SUM(CAST(REPLACE(o.total, ',', '') AS FLOAT)) AS totalSale "
			+ "FROM Orders o "
            + "WHERE o.store.storeid = :storeId "
            + "AND o.orderCreatedDate >= :startDate "
            + "AND o.orderCreatedDate < :endDate "
            + "GROUP BY o.state "
            + "ORDER BY stateCount DESC "
            + "LIMIT 5")
    List<Object[]> findTop5StatesByOrderCount(@Param("storeId") int storeId,
                                               @Param("startDate") Timestamp startDate,
                                               @Param("endDate") Timestamp endDate);
    
    @Query("SELECT new map(o.total as total, o.orderCreatedDate as orderCreatedAt) FROM Orders o WHERE o.store.storeid = :storeid")
    List<Object> findTotalAndOrderCreatedAtByStoreid(@Param("storeid") int storeid);

}
