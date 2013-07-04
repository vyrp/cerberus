from google.appengine.api import users
import webapp2
import main
from models.Device import Device
from models.Transaction import Transaction


class ViewTableHandler(webapp2.RequestHandler):
    def get(self):
        transactions = {}
        devices = Device.get_all()
        for device in devices:
            transactions.update({device.name: Transaction.query(ancestor=device.key).fetch()})

        values = {
            "user": users.get_current_user() or "User",
            "transactions": transactions
        }

        self.response.write(main.render("templates/borrowTable.html", values))