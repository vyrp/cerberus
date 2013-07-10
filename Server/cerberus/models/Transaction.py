from google.appengine.ext import ndb


class Transaction(ndb.Model):
    start = ndb.IntegerProperty()
    name = ndb.StringProperty()
    end = ndb.IntegerProperty(default=-1)

    @classmethod
    def get_last(cls, device):
        return cls.query(ancestor=device.key).order(-cls.start).get()

    @classmethod
    def get_all(cls, device):
        return cls.query(ancestor=device.key).order(-cls.start).fetch()
