from woocommerce import API
import json
from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.parsers import JSONParser 
import pandas as pd
from datetime import datetime
from statsmodels.tsa.statespace.sarimax import SARIMAX
from opentelemetry.trace import get_current_span

@api_view(['GET', 'POST', 'DELETE'])
def callll(request):
    print("traceparent:", request.headers.get("traceparent"))
    span = get_current_span()
    trace_id = format(span.get_span_context().trace_id, '032x')
    span_id = format(span.get_span_context().span_id, '016x')

    print(f"[TraceID: {trace_id}] [SpanID: {span_id}] Django received request")
    client_data = JSONParser().parse(request)
    print(client_data)
    return JsonResponse({"reply":woocommerce_orders(client_data["domain"],client_data["consumerKey"], client_data["secretConsumerKey"],client_data["lastModifiedDate"],"v3")},safe=False)
     
@api_view(['GET', 'POST', 'DELETE'])
def getforecasting(request):
    client_data = JSONParser().parse(request)

    for each in client_data:
         each['orderCreatedAt'] = datetime.fromtimestamp(each['orderCreatedAt'] / 1000.0)

    df = pd.DataFrame(client_data)

# Convert 'orderCreatedAt' column to datetimebin
    
    df['orderCreatedAt'] = pd.to_datetime(df['orderCreatedAt'])

    # Convert 'total' column to numeric
    df['total'] = pd.to_numeric(df['total'])

    # Set 'orderCreatedAt' as index
    df.set_index('orderCreatedAt', inplace=True)

    monthly_sales = df['total'].resample('M').sum()

    print(monthly_sales)

    model = SARIMAX(monthly_sales, order=(1, 0, 0), seasonal_order=(1, 0, 0, 12))
    model_fit = model.fit()

    forecast = model_fit.forecast(steps=12)

    # print(client_data)
    print("Forecasted Sales:")
    print(forecast)

    last_month = monthly_sales.index[-1].month
    forecast_months = [(last_month + i) % 12 or 12 for i in range(1, 13)]

    # Create a list of dictionaries containing month and sales
    forecast_data = [{'month': month, 'sales': sales} for month, sales in zip(forecast_months, forecast)]

    # Convert list of dictionaries to JSON
    forecast_json = json.dumps(forecast_data)
    
    return JsonResponse({"reply":forecast_json})

def woocommerce_orders(domain, consumerkey, consumersecretkey,max_date, woocommerce_version):
    wcapi = API(
        url=domain,
        consumer_key=consumerkey,
        consumer_secret=consumersecretkey,
        wp_api=True,
        version="wc/"+ woocommerce_version,
        query_string_auth=True, # Force Basic Authentication as query string true and using under HTTPS
        timeout=1000
    )

    if(max_date == "Not Found"):
            obj = wcapi.get("orders", params={"per_page": 100})
            print(obj)
    else:
        print(max_date)
        obj = wcapi.get("orders", params={"per_page": 100, "modified_after":max_date})

    obj = json.loads(obj.text)
    print(obj)
    return obj


