import os

import webapp2
import jinja2
from controllers.HttpTableHandler import HttpTableHandler
from controllers.ViewTableHandler import ViewTableHandler

_JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
    extensions=['jinja2.ext.autoescape'])

app = webapp2.WSGIApplication([
    ('/', ViewTableHandler),
    ('/get', HttpTableHandler),
    ('/post', HttpTableHandler)
], debug=True)


def render(template, values):
    return _JINJA_ENVIRONMENT.get_template(template).render(values)