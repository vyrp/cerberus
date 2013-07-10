# coding: utf-8

import cgi
import time
import webapp2
from models.Transaction import Transaction
from models.Device import Device


def serialize(transactions):
    return "|".join([";".join(map(str, [t.start, t.name.encode("utf8"), t.end])) for t in transactions])


def escape(string):
    return cgi.escape(string.replace(";", "_").replace(":", "_").replace("|", "_"))


def now():
    return int(1000 * time.time())


class HttpTableHandler(webapp2.RequestHandler):
    def get(self):
        if not self.request.get("device"):
            return

        device = Device.get_by_name(escape(self.request.get("device")))
        self.response.write(serialize(Transaction.get_all(device)))

    def post(self):
        if not self.request.get("device"):
            return

        device_name = escape(self.request.get("device"))
        device = Device.query(Device.name == device_name).get()

        if self.request.get("borrower"):
            Transaction(parent=device.key, name=escape(self.request.get("borrower")), start=now()).put()
            self.response.write("Created.")

        elif self.request.get("update"):
            last = Transaction.get_last(device)
            last.end = now()
            last.put()
            self.response.write(last.name)

        else:
            Device(name=device_name).put()
            self.response.write("Created.")