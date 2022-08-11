import struct

import happybase


class HBaseUtil():
    def __init__(self):
        self.conf = None
        self.connection = None
        self.admin = None

    def get_connection(self):
        if self.connection is None:
            self.connection = happybase.Connection(host="master")
        return self.connection

    def get_table(self, table_name):
        return self.get_connection().table(table_name)

    def scan_table(self, table_name, cols: dict, filter=None):
        scan = self.get_connection().table(table_name).scan(filter=filter)
        results = {}
        for k, v in scan:
            results[k.decode()] = {}
            for c_name in cols.keys():
                if cols.get(c_name) == "int":
                    results[k.decode()][c_name.decode()] = struct.unpack(">i", v[c_name])[0]
                if cols.get(c_name) == "double":
                    results[k.decode()][c_name.decode()] = struct.unpack(">d", v[c_name])[0]
                else:
                    results[k.decode()][c_name.decode()] = v[c_name].decode()
        return results

    def close(self):
        if self.connection is not None:
            self.connection.close()
        self.connection = None
