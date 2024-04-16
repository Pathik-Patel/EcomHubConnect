import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import './../Styles/orders.css';
import './../Styles/chart.css';
import Nav from './Nav';

import UserService from '../Services/UserService';
import BarGraph from './BarGraph';

const Orders = () => {
  const location = useLocation();
  const ordersData = location.state?.orders || [];
  const storeid = location.state?.storeid || [];
  const [orders, setOrders] = useState(ordersData);
  const [selectedStatus, setSelectedStatus] = useState('');
  const [error, setError] = useState(null);
  const [data, setData] = useState(null);
  const [forecasteddata, setForecasteddata] = useState(null);
  const [temp, setTemp] = useState(false);
  const [currentProductIndex, setCurrentProductIndex] = useState(0);
  const [currentStateIndex, setCurrentStateIndex] = useState(0);
  const [duration, setDuration] = useState('This month');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [showCustom, setShowCustom] = useState(false);
  const currentDate = new Date();
  const currentMonth = currentDate.getMonth() + 1; // Add 1 since getMonth() returns 0-based index
  const currentYear = currentDate.getFullYear();

  // Split forecasting data into current year and next year
  // const currentYearData = forecasteddata.slice(currentMonth - 1);
  // const nextYearData = forecasteddata.slice(0, currentMonth - 1);

  // Combine the data for rendering
  // const combinedData = currentYearData.concat(nextYearData);
  const inttomonth = {
    1:"January",
    2:"February",
    3:"March",
    4:"April",
    5:"May",
    6:"June",
    7:"July",
    8:"August",
    9:"September",
    10:"October",
    11:"November",
    12:"December"

  };
  

  

  const handleDurationChange = (event) => {
    const selectedDuration = event.target.value;
    setDuration(selectedDuration);
    setShowCustom(selectedDuration === 'Custom');
    if (selectedDuration === 'This month') {
      const currentDate = new Date();
      const startOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
      setStartDate(startOfMonth.toISOString().slice(0, 10));
      setEndDate(currentDate.toISOString().slice(0, 10));
    
    } else if (selectedDuration === 'Last 3 Months') {
      const currentDate = new Date();
      const threeMonthsAgo = new Date(currentDate);
      threeMonthsAgo.setMonth(currentDate.getMonth() - 3);
      setStartDate(threeMonthsAgo.toISOString().slice(0, 10));
      setEndDate(currentDate.toISOString().slice(0, 10));
    
    } else if (selectedDuration === 'Last 6 Months') {
      const currentDate = new Date();
      const sixMonthsAgo = new Date(currentDate);
      sixMonthsAgo.setMonth(currentDate.getMonth() - 6);
      setStartDate(sixMonthsAgo.toISOString().slice(0, 10));
      setEndDate(currentDate.toISOString().slice(0, 10));

    }
    
    
  };

  const handleStartDateChange = (event) => {
    setStartDate(event.target.value);
  };

  const handleEndDateChange = (event) => {
    setEndDate(event.target.value);
  };


  useEffect( () => {
    setOrders(ordersData); // Reset orders when ordersData changes
    const currentDate = new Date();
  const startOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
  setStartDate(startOfMonth.toISOString().slice(0, 10));
  setEndDate(currentDate.toISOString().slice(0, 10));
 
  }, [ordersData]);

  useEffect(() => {
    // Call showinsights only when both startDate and endDate are properly set
    if (startDate && endDate) {
      showinsights();
    }
  }, [startDate, endDate]);

  const showinsights = () => {
    UserService.storeinsightss(storeid, {"startdate":startDate, "enddate":endDate})
      .then((response) => {
        setData(JSON.parse(response));
        console.log(data);
        setTemp(true);

      })
      .catch((error) => {
        setError(error);
      });
  }

  

  function ProductCard({ product , currentindex}) {
    return (
      <div className="product-card">
        
        <p className='text-center'># {currentindex + 1}</p>
        <p className='text-center'><span className='front-label'>Product: </span>{product.productName}</p>
        <p className='text-center'><span className='front-label'>Quantities Sold: </span>{product.quantitySold}</p>
        <p className='text-center'><span className='front-label'>Revenue Generated: </span>{product.revenue}$</p>
      </div>
    );
  }

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

  const goToNextProduct = () => {
    setCurrentProductIndex((prevIndex) =>
      prevIndex === JSON.parse(data.topfiveproducts).length - 1 ? 0 : prevIndex + 1
    );
  };

  const goToPreviousProduct = () => {
    setCurrentProductIndex((prevIndex) =>
      prevIndex === 0 ? JSON.parse(data.topfiveproducts).length - 1 : prevIndex - 1
    );
  };

  function StateCard({ stateData, currentindex }) {
    return (
      <div className="state-card">
         <p className='text-center'># {currentindex + 1}</p>
        <p className='text-center'><span className='front-label'>State: </span>{stateData[0]}</p>
        <p className='text-center'><span className='front-label'>Total Orders:</span> {stateData[1]}</p>
        <p className='text-center'><span className='front-label'>Total Sales:</span> {stateData[3]}$</p>
      </div>
    );
  }



  const goToNextState = () => {
    setCurrentStateIndex((prevIndex) =>
      prevIndex === data.topfivestatesList.length - 1 ? 0 : prevIndex + 1
    );
  };

  const goToPreviousState = () => {
    setCurrentStateIndex((prevIndex) =>
      prevIndex === 0 ? data.topfivestatesList.length - 1 : prevIndex - 1
    );
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
};

const filterOrdersByDateRange = (order) => {
  const orderDate = new Date(order.orderCreatedDate);
  return orderDate >= new Date(startDate) && orderDate <= new Date(endDate);
};

const showforecasting = ( ) => {
  UserService.storeforecasting(storeid)
      .then((response) => {
        var insidedata = JSON.parse(response);
        var forecasting = JSON.parse(insidedata["reply"])
        
        // setData(JSON.parse(response));
        
        var monthwisejson = {};
        
        
        forecasting.forEach(element => {
          var givenyear = 0;
          if(element["month"]>currentMonth){

            givenyear = currentYear;
          }
          else{
            givenyear = currentYear + 1; 
          }
          monthwisejson[inttomonth[element["month"]] + " " + givenyear.toString()] = parseInt(element["sales"]);
        });

        setForecasteddata(monthwisejson);
        console.log(monthwisejson);
        // console.log(monthwisejson);
        

      })
      .catch((error) => {
        setError(error);
      });
}

  return (
    <div>
      <Nav />
      <div className="orders-container">
        <div>
        <div>
          <button className="forecastbutton" onClick={showforecasting}>+ Forecasting</button>
          <div className='forecasting'>
          {/* { forecasteddata && Object.keys(forecasteddata).map(key => (
          <li key={key}><strong>{key}:</strong> {forecasteddata[key]}$</li>
        ))} */}

        { forecasteddata && <BarGraph data={forecasteddata} containerId="forecasting"/>}
            
          </div>
      <label htmlFor="duration">Choose Duration for insights:</label>
      <select id="duration" value={duration} onChange={handleDurationChange}>
        <option value="This month">This month</option>
        <option value="Last 3 Months">Last 3 Months</option>
        <option value="Last 6 Months">Last 6 Months</option>
        <option value="Custom">Custom</option>
      </select>
      {showCustom && (
        <div>
          <label htmlFor="startDate">Start Date:</label>
          <input type="date" id="startDate" value={startDate} onChange={handleStartDateChange} />
          <label htmlFor="endDate">End Date:</label>
          <input type="date" id="endDate" value={endDate} onChange={handleEndDateChange} />
        </div>
      )}
      {/* <button onClick={handleApply}>Apply</button> */}
    </div>
          {/* <button onClick={showinsights}>View Insights</button> */}
          {temp ? (
            <div className="insights">
              {/* Total Orders and Total Sales */}
              <div className="totalordersandsales">
                <div className="totalorders">
                  
                  <h1>{data.totalordersandsale.totalOrders}</h1>
                  <p>Total Orders</p>
                </div>
                <div className="totalsales">
                 
                  <h1>{data.totalordersandsale.totalSale}$</h1>
                  <p>Total Sales</p>
                </div>



              </div>

              {/* Top Five Products */}


              <div className="product-carousel" >
                <h2>Best Selling Products</h2>
                <div className="carousel-container">
                <div className="icon-holder">
                    <button className="prev-button" onClick={goToPreviousProduct}>
                      &#10094;
                    </button>
                    
                  </div>
                  <div className="carousel-slide active">
                    {data.topfiveproducts &&
                      <ProductCard product={JSON.parse(data.topfiveproducts)[currentProductIndex]} currentindex={currentProductIndex} />
                    }
                  </div>
                  <div className="icon-holder">
                    
                    <button className="next-button" onClick={goToNextProduct}>
                      &#10095;
                    </button>
                  </div>

                </div>
              </div>
              <div className="state-carousel">
                <h2>Top Five States</h2>
                <div className="carousel-container">
                <div className="icon-holder">
                    <button className="prev-button" onClick={goToPreviousState}>
                      &#10094;
                    </button>
                    
                  </div>
                  <div className="carousel-slide active">
                    {data.topfivestatesList &&
                      <StateCard stateData={data.topfivestatesList[currentStateIndex] } currentindex={currentStateIndex} />
                    }
                  </div>
                  <div className="icon-holder">
                    
                    <button className="next-button" onClick={goToNextState}>
                      &#10095;
                    </button>
                  </div>
                </div>




              </div>
              {/* Top Five States List */}
              {/* <div>
            <h2>Top Five States List</h2>
            {data.topfivestatesList && data.topfivestatesList.map((stateData, index) => (
              <div key={index}>
                <p>State: {stateData[0]}</p>
                <p>Total Orders: {stateData[1]}</p>
                <p>Total Sales: {stateData[2]}</p>
              </div>
            ))}
          </div> */}
            </div>
          ) : (
            <p></p>
          )}
        </div>
        <h2 className='ordersheading'>Orders</h2>
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
              <th>Date Created</th>
            </tr>
          </thead>
          <tbody>
            {orders.filter(filterOrdersByDateRange).map(order => (
              <tr key={order.id}>
                <td>{order.orderid}</td>
                <td>{order.customerName}</td>
                <td>{order.email}</td>
                <td>{order.phone}</td>
                <td>{order.address}</td>
                <td>{order.total}</td>
                <td>{toCamelCase(order.status)}</td>
                <td>{formatDate(order.orderCreatedDate)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Orders;
