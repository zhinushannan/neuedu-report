from util.hbase_util import HBaseUtil

cols = {
    b"data:date": "str",
    b"data:maxTemperature": "double",
    b"data:minTemperature": "double"
}

hbase = HBaseUtil()

filter = {
    b'data:date': b'31/12/2013'
}

result = hbase.scan_table("weather", cols, "SingleColumnValueFilter('data', 'date', =, 'binary:31/12/2013')")

for k in result.keys():
    print(k, result.get(k))

hbase.close()
