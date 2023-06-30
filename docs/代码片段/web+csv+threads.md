```python
import csv
import json
import requests
from concurrent.futures import ThreadPoolExecutor

def post_data(data):
    url = "http://example.com/api"
    headers = {'Content-Type': 'application/json'}
    json_data = json.dumps(data)
    response = requests.post(url, headers=headers, data=json_data)
    if response.status_code != 200:
        print(f"Error: {response.status_code}")
    else:
        print(f"Data sent: {json_data}")

def process_csv(file_path):
    with open(file_path, newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) # skip header
        with ThreadPoolExecutor(max_workers=5) as executor:
            for row in reader:
                data = {'id': row[0], 'name': row[1], 'email': row[2]}
                executor.submit(post_data, data)

if __name__ == '__main__':
    file_path = 'data.csv'
    process_csv(file_path)
```

