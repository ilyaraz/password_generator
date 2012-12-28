import hashlib
import sys
import getpass
import os

def shellquote(s):
    return "'" + s.replace("'", "'\\''") + "'"

symb = []
symb += [chr(x + ord('A')) for x in xrange(26)]
symb += [chr(x + ord('a')) for x in xrange(26)]
symb += [chr(x + ord('0')) for x in xrange(10)]
symb += [x for x in "!@#$%^&*()-_=+`~[{]};:'\"\\|,<.>/?"]

print len(symb)

password = getpass.getpass()

h = hashlib.new('sha512')
h.update(password)
h.update(sys.argv[1])
h.update('')
a = list(h.digest())
num = 0
for c in a:
    num = num * 256 + ord(c)
password = ""
for i in xrange(15):
    password += symb[num % 94]
    num = num / 94
os.system("echo %s | pbcopy" % shellquote(password))
raw_input("press any key")
os.system("echo blahblah | pbcopy")
