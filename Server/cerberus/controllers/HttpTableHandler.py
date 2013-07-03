from google.appengine.api import users
import webapp2
import main
from models.Transaction import Transaction


def serialize(transactions):
    return "|".join([";".join([t.start, t.name, t.end]) for t in transactions])


class ViewTableHandler(webapp2.RequestHandler):
    def get(self):
        query = Transaction.query()
        self.response.write(serialize(query.fetch(100)))

    def post(self):
        pass