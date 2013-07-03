from google.appengine.api import users
import webapp2
import main
from models.Transaction import Transaction


class ViewTableHandler(webapp2.RequestHandler):
    def get(self):
        query = Transaction.query()

        values = {
            "user": users.get_current_user() or "User",
            "transactions": query.fetch(100)
        }

        self.response.write(main.render("templates/borrowTable.html", values))