import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

const BarGraph = ({ data, containerId }) => {
  const chartRef = useRef(null);
  const chartInstanceRef = useRef(null);

  useEffect(() => {
    const ctx = chartRef.current.getContext('2d');

    // Destroy existing chart instance if it exists
    if (chartInstanceRef.current) {
      chartInstanceRef.current.destroy();
    }

    // Create new chart instance
    chartInstanceRef.current = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(data),
        datasets: [{
          label: 'Sales in $',
          data: Object.values(data),
          backgroundColor: 'rgba(54, 162, 235, 0.5)', // Blue color for bars
          borderColor: 'rgba(54, 162, 235, 1)', // Border color for bars
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true // Start y-axis from 0
          }
        }
      }
    });

    return () => {
      // Cleanup function to destroy chart instance when component unmounts
      if (chartInstanceRef.current) {
        chartInstanceRef.current.destroy();
      }
    };
  }, [data, containerId]);

  return <canvas ref={chartRef} id={containerId} />;
};

export default BarGraph;
