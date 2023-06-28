
function createChart2(chartId,h_data,in_v_data,ex_v_data,name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  var backgroundColors = ['rgba(0, 207, 255, 0.2)', 'rgba(255, 99, 132, 0.2)'];
		  var borderColors = ['rgba(0, 207, 255, 1)', 'rgba(255, 99, 132, 1)'];

		  var myChart = new Chart(ctx, {
			    type: 'bar',
			    data: {
			      labels: h_data,
			      datasets: [{
			        label: '수입',
			        data: in_v_data,
			        backgroundColor: backgroundColors[0],
			        borderColor: borderColors[0],
			        borderWidth: 1,
			        barPercentage: 1.0, // Adjust the bar width as desired
			        categoryPercentage:0.7,
			        barSpacing: '10px'
			      },
			      {
			        label: '소비',
			        data: ex_v_data,
			        backgroundColor: backgroundColors[1],
			        borderColor: borderColors[1],
			        borderWidth: 1,
			        barPercentage: 1.0, // Adjust the bar width as desired
			        categoryPercentage: 0.7,
			        barSpacing: '10px'
			      }]
			    },
			    options: {
			      responsive: true,
			      maintainAspectRatio: false,
			      scales: {
			        y: {
			          beginAtZero: true
			        }
			      }
			    }
			  });		
		}
	function createChart3(chartId,h_data,min_v_data,avg_v_data,name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  var backgroundColors = ['rgba(0, 207, 255, 0.2)', 'rgba(255, 99, 132, 0.2)'];
		  var borderColors = ['rgba(0, 207, 255, 1)', 'rgba(255, 99, 132, 1)'];

		  var myChart = new Chart(ctx, {
			    type: 'bar',
			    data: {
			      labels: h_data,
			      datasets: [{
			        label: '연령별 최소 소비',
			        data: min_v_data,
			        backgroundColor: backgroundColors[0],
			        borderColor: borderColors[0],
			        borderWidth: 1,
			        barPercentage: 1.0, // Adjust the bar width as desired
			        categoryPercentage:0.7,
			        barSpacing: '10px'
			      },
			      {
			        label: '연령별 평균 소비',
			        data: avg_v_data,
			        backgroundColor: backgroundColors[1],
			        borderColor: borderColors[1],
			        borderWidth: 1,
			        barPercentage: 1.0, // Adjust the bar width as desired
			        categoryPercentage: 0.7,
			        barSpacing: '10px'
			      }]
			    },
			    options: {
			      responsive: true,
			      maintainAspectRatio: false,
			      scales: {
			        y: {
			          beginAtZero: true
			        }
			      }
			    }
			  });		
		}
	function createChart(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  var backgroundColors = v_data.map((_, index) => index === v_data.length - 1 && chartId === 'chart5' ? 'rgba(0, 207, 255, 0.8)' : 'rgba(0, 207, 255, 0.2)');
		  var borderColors = v_data.map((_, index) => index === v_data.length - 1 && chartId === 'chart5' ? 'rgba(0, 207, 255, 1)' : 'rgba(0, 207, 255, 1)');

		  var myChart = new Chart(ctx, {
		    type: 'bar',
		    data: {
		      labels: h_data,
		      datasets: [{
		        label: name,
		        data: v_data,
		        backgroundColor: backgroundColors,
		        borderColor: borderColors,
		        borderWidth: 1,
		        barPercentage: 1.0,
		        categoryPercentage:1.0,
		        barSpacing: '10px'
		      }]
		    },
		    options: {
		      responsive: true,
		      maintainAspectRatio: false,
		      scales: {
		        y: {
		          beginAtZero: true
		        }
		      }
		    }
		  });
		}
	function createChart6(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  var backgroundColors = v_data.map((_, index) => index === v_data.length - 1 && chartId === 'chart5' ? 'rgba(0, 207, 255, 0.8)' : 'rgba(0, 207, 255, 0.2)');
		  var borderColors = v_data.map((_, index) => index === v_data.length - 1 && chartId === 'chart5' ? 'rgba(0, 207, 255, 1)' : 'rgba(0, 207, 255, 1)');

		  var myChart = new Chart(ctx, {
		    type: 'bar',
		    data: {
		      labels: h_data,
		      datasets: [{
		        label: name,
		        data: v_data,
		        backgroundColor: backgroundColors,
		        borderColor: borderColors,
		        borderWidth: 1,
		        barPercentage: 1.0,
		        categoryPercentage:0.6,
		        barSpacing: '10px'
		      }]
		    },
		    options: {
		      responsive: true,
		      maintainAspectRatio: false,
		      scales: {
		        y: {
		          beginAtZero: true
		        }
		      }
		    }
		  });
		}
	
	function createChart1(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  var total = v_data.reduce((acc, value) => acc + value, 0);
		  var percentages = v_data.map(value => ((value / total) * 100).toFixed(2) + '%');

		  var myChart = new Chart(ctx, {
		    type: 'doughnut',
		    data: {
		      labels: h_data,
		      datasets: [{
		        label: name,
		        data: v_data,
		        backgroundColor: ['rgba(0, 207, 255, 0.8)','rgba(255, 99, 132, 0.8)',
		        	'rgba(255, 159, 64, 0.8)','rgba(255, 205, 86, 0.8)',
		        	'rgba(75, 192, 192, 0.8)','rgba(54, 162, 235, 0.8)',
		        	'rgba(153, 102, 255, 0.8)','rgba(255, 0, 0, 0.8)'],
		      }]
		    },
		    options: {

		      responsive: true,
		      maintainAspectRatio: false,
		      
		      plugins: {
		        tooltip: {
		          callbacks: {
		            label: function(context) {
		              var value = context.dataset.data[context.dataIndex];
		              var percentage = percentages[context.dataIndex];
		              return 'Value: ' + value.toLocaleString() + ', Percentage: ' + percentage;
		            }
		          }
		        }
		      }
		    },
		    datalabels: {
          align: 'center',
          anchor: 'center',
          font: {
            weight: 'bold'
          }
        }

		  });
		// Create the table container
// 		  var tableContainer = document.createElement('div');
// 		  tableContainer.classList.add('table-container');

// 		  // Create the table rows
// 		  var tableRows = h_data.map((category, index) => {
			  
// 			var row = document.createElement('div');
// 		    var row1 = document.createElement('div');
// 		    row1.classList.add('table-row');
// 		    var row2 = document.createElement('div');
// 		    row2.classList.add('table-row');
// 		    var categoryCell = document.createElement('h4');
// 		    categoryCell.textContent = category;
// 		    var percentageCell = document.createElement('p');
// 		    percentageCell.textContent =percentages[index];
// 		    var amountCell = document.createElement('p');
// 		    amountCell.textContent =  v_data[index].toLocaleString();

// 		    row1.appendChild(categoryCell);
// 		    row1.appendChild(percentageCell);
// 		    row2.appendChild(amountCell);
		    
// 		    row.appendChild(row1);
// 		    row.appendChild(row2);

// 		    return row;
// 		  });

// 		  // Append the table rows to the table container
// 		  tableRows.forEach(function (row) {
// 		    tableContainer.appendChild(row);
// 		  });

// 		  var chartContainer = document.getElementById(chartId);

// 		  chartContainer.insertAdjacentElement('afterend', tableContainer);

		}
		function createChart4(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		 
			  var myChart = new Chart(ctx, {
			    type: 'bar',
			    data: {
			      labels: h_data,
			      datasets: [{
			        label: name,
			        data: v_data,
			        backgroundColor: ['rgba(0, 207, 255, 0.8)', 'rgba(255, 99, 132, 0.8)'],
			      }]
			    },
			    options: {
			      responsive: true,
			      maintainAspectRatio: false,
			      indexAxis: 'y', // Display the chart vertically
			      plugins: {
			        legend: {
			          display: false,
			        },
			        datalabels: {
			          anchor: 'end',
			          align: 'start',
			          color: 'black',
			          font: {
			            weight: 'bold'
			          },
			          formatter: function(value) {
			            return value.toLocaleString();
			          }
			        }
			      }
			    }
			  });
			}
function createChart5(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		 
		  var myChart = new Chart(ctx, {
		    type: 'line',
		    data: {
		      labels: h_data,
		      datasets: [{
		        label: name,
		        data: v_data,
		        backgroundColor:'rgba(0, 207, 255, 0.3)',
		        borderColor: 'rgba(0, 207, 255, 1)',
		        borderWidth: 1,
		        barPercentage: 1.0,
		        categoryPercentage: 1.0,
		        barSpacing: '10px'
		      }]
		    },
		    options: {
		      responsive: true,
		      maintainAspectRatio: false,
		      scales: {
				x: {
		          grid: {
		            display: false, // Remove the x-axis grid lines
		          },
		        },
		        y: {
		          beginAtZero: true,
		          grid: {
		            display: false, // Remove the y-axis grid lines
		          },
		        },
		        
		      }
		    }
		  });
		}
		function createChart7(chartId, h_data, v_data, name) {
		  var ctx = document.getElementById(chartId).getContext('2d');
		  
		  var myChart = new Chart(ctx, {
		    type: 'bar',
		    data: {
		      labels: h_data,
		      datasets: [{
		        label: name,
		        data: v_data,
		        backgroundColor: ['rgba(255, 205, 86, 0.8)','rgba(54, 162, 235, 0.8)',],
		       
		        borderWidth: 1,
		        barPercentage: 1.0,
		        categoryPercentage:0.4,
		        barSpacing: '10px'
		      }]
		    },
		    options: {
		      responsive: true,
		      maintainAspectRatio: false,
		      scales: {
		        y: {
		          beginAtZero: true
		        }
		      }
		    }
		  });
		}