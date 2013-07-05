from google.appengine.api import users
import webapp2
import main
from models.Device import Device
from models.Transaction import Transaction
from datetime import datetime


def convert_timestamps_to_dates(transactions):
    return [{"start": str(datetime.fromtimestamp(t.start / 1000)) if t.start > 0 else "-",
             "name": t.name,
             "end": str(datetime.fromtimestamp(t.end / 1000)) if t.end > 0 else "-"}
            for t in transactions]


class ViewTableHandler(webapp2.RequestHandler):
    def get(self):
        transactions = {}
        devices = Device.get_all()
        for device in devices:
            transactions.update({device.name: convert_timestamps_to_dates(Transaction.query(ancestor=device.key).fetch())})

        values = {
            "user": users.get_current_user() or "User",
            "transactions": transactions
        }

        self.response.write(main.render("templates/borrowTable.html", values))