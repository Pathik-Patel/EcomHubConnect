import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './../Styles/orders.css';
import Nav from './Nav';

const Orders = () => {
    const location = useLocation();
    const ordersData = location.state?.orders || [];
    const [orders, setOrders] = useState(ordersData);
    const [selectedStatus, setSelectedStatus] = useState('');

    useEffect(() => {
        setOrders(ordersData); // Reset orders when ordersData changes
    }, [ordersData]);

    const handleStatusChange = (e) => {
        const status = e.target.value.toLowerCase(); // Convert to lowercase
        setSelectedStatus(status);
        if (status === '') {
            setOrders(ordersData);
        } else {
            const filteredOrders = ordersData.filter(order => order.status.toLowerCase() === status);
            setOrders(filteredOrders);
        }
    };

    // Function to convert string to camel case
    const toCamelCase = (str) => {
        return str.replace(/\b(\w)/g, match => match.toUpperCase()).replace(/\s+/g, '');
    };

    return (
        <div>
            <Nav />
            <div className="orders-container">
                <h2>Orders</h2>
                <div className="filter-container">
                    <label htmlFor="status-filter">Filter by Status:</label>
                    <select id="status-filter" value={selectedStatus} onChange={handleStatusChange}>
                        <option value="">All</option>
                        <option value="completed">Completed</option>
                        <option value="cancelled">Cancelled</option>
                        <option value="processing">Processing</option>
                    </select>
                </div>
                <table className="orders-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Address</th>
                            <th>Total</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map(order => (
                            <tr key={order.id}>
                                <td>{order.orderid}</td>
                                <td>{order.customerName}</td>
                                <td>{order.email}</td>
                                <td>{order.phone}</td>
                                <td>{order.address}</td>
                                <td>{order.total}</td>
                                <td>{toCamelCase(order.status)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Orders;
