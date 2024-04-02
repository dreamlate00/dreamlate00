读取

```python
import csv

file_path = 'data.csv'  # 替换为你的CSV文件路径

with open(file_path, 'r', newline='') as file:
    reader = csv.reader(file)
    for row in reader:
        print(row)
```

写入

```python
import csv

file_path = 'data.csv'  # 替换为你要写入的CSV文件路径
data = [
    ['Name', 'Age', 'City'],
    ['John', '30', 'New York'],
    ['Alice', '25', 'London'],
    ['Bob', '35', 'Paris']
]

with open(file_path, 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(data)

print("CSV数据已成功写入文件。")
```

