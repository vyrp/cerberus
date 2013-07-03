import cgi
import webapp2
from models.Transaction import Transaction


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