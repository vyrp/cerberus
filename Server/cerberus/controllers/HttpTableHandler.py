from google.appengine.api import users
import webapp2
import main
from models.Transaction import Transaction
import cgi

def serialize(transactions):
    return "|".join([";".join(map(str, [t.start, t.name, t.end])) for t in transactions])


class HttpTableHandler(webapp2.RequestHandler):
    def get(self):
        query = Transaction.query()
        self.response.write(serialize(query.fetch(100)))

    def post(self):
        t = Transaction(name=cgi.escape(self.request.get("borrower")))
        t.put()
        self.redirect("/")