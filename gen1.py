import hashlib
import sys
import getpass
import os

if len(sys.argv) != 3:
    raise Exception("usage: python gen1.py <clue> <password-length>")

clue = sys.argv[1]
length = int(sys.argv[2])

def shellquote(s):
    return "'" + s.replace("'", "'\\''") + "\c'"

symb = [chr(x) for x in xrange(33, 127)]

password = getpass.getpass()

h = hashlib.new('sha512')
h.update(password)
print h.hexdigest()[:5]

h = hashlib.new('sha512')
h.update(password)
h.update(clue)
a = list(h.digest())
num = 0
for c in a:
    num = num * 256 + ord(c)
password = ""
for i in xrange(length):
    password += symb[num % len(symb)]
    num = num / len(symb)
print password
