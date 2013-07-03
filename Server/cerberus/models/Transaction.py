from google.appengine.ext import ndb


class Transaction(ndb.Model):
    start = ndb.DateTimeProperty(auto_now_add=True)
    name = ndb.StringProperty()
    end = ndb.DateTimeProperty()
